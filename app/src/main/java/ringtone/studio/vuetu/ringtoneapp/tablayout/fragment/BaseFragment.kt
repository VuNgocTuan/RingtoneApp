package ringtone.studio.vuetu.ringtoneapp.tablayout.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.Toast
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import ringtone.studio.vuetu.ringtoneapp.R
import ringtone.studio.vuetu.ringtoneapp.repository.model.Ringtone
import ringtone.studio.vuetu.ringtoneapp.repository.sqlite.RingtoneDatabaseHelper
import ringtone.studio.vuetu.ringtoneapp.tablayout.fragment.adapter.NewRingtoneAdapter
import ringtone.studio.vuetu.ringtoneapp.utils.Constants

/**
 * Created by FRAMGIA\vu.ngoc.tuan on 23/04/2018.
 */
open class BaseFragment : Fragment() {
    private lateinit var mRecyclerView: RecyclerView
    private lateinit var mProgressBar: ProgressBar
    private lateinit var mAdapter: NewRingtoneAdapter
    var mDatabase: RingtoneDatabaseHelper? = null


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mDatabase = RingtoneDatabaseHelper.getInstance(context?.applicationContext)
        return inflater.inflate(R.layout.fragment_new_ringtone, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initView(view)
    }

    private fun initView(view: View) {
        val layoutManager = LinearLayoutManager(context, LinearLayout.VERTICAL, false)
        mRecyclerView = view.findViewById(R.id.recycler_main)
        mRecyclerView.layoutManager = layoutManager
        mAdapter = NewRingtoneAdapter()
        mRecyclerView.adapter = mAdapter

        mProgressBar = view.findViewById(R.id.progress_bar)
    }

    fun requestDataFromRemote(url: String, tableName:String) {
        mProgressBar.visibility = View.VISIBLE
        Observable.create<Document>({ emitter ->
            Thread({
                try {
                    val doc = Jsoup.connect(url).timeout(0).get()
                    emitter.onNext(doc)
                    emitter.onComplete()
                } catch (t: Throwable) {
                    emitter.onError(t)
                }
            }).start()
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<Document> {
                    override fun onSubscribe(d: Disposable) {

                    }

                    override fun onError(e: Throwable) {
                        e.printStackTrace()
                        Toast.makeText(context, "Cache Data Loaded", Toast.LENGTH_LONG).show()
                        mDatabase?.getRingtoneList(tableName)?.let {
                            mAdapter.setData(it)
                        }
                        mProgressBar.visibility = View.GONE
                    }

                    override fun onNext(doc: Document) {
                        val newElement = doc.select(Constants.ELEMENT)
                        if (newElement.size > 0) {
                            mDatabase?.deleteAllRecords(tableName)
                            for (query in newElement) {
                                val ringtoneName = query.select(Constants.KEY_RINGTONE_NAME).text()
                                val ringtoneAuthor = query.select(Constants.KEY_RINGTONE_AUTHOR).text()
                                val ringtoneLink = query.select(Constants.KEY_RINGTONE_URL)
                                        .attr(Constants.ATTRIBUTE_RINGTONE_URL)
                                mDatabase?.addNewestRingtone(
                                        Ringtone(0, ringtoneName,
                                                ringtoneAuthor,
                                                ringtoneLink)
                                )
                            }
                        }
                        println("Element size: " + newElement.size)
                    }

                    override fun onComplete() {
                        mDatabase?.getRingtoneList(tableName)?.let {
                            mAdapter.setData(it)
                        }
                        mProgressBar.visibility = View.GONE
                    }

                })
    }
}
