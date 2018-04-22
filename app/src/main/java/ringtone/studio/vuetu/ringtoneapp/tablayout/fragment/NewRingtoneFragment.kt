package ringtone.studio.vuetu.ringtoneapp.tablayout.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.select.Elements
import ringtone.studio.vuetu.ringtoneapp.R
import ringtone.studio.vuetu.ringtoneapp.repository.model.Ringtone
import ringtone.studio.vuetu.ringtoneapp.repository.sqlite.RingtoneDatabaseHelper
import ringtone.studio.vuetu.ringtoneapp.tablayout.fragment.adapter.NewRingtoneAdapter

/**
 * Created by vungoctuan on 4/11/18.
 */
class NewRingtoneFragment : Fragment() {
    private lateinit var mRecyclerView: RecyclerView
    private lateinit var mAdapter: NewRingtoneAdapter
    private var mDatabase: RingtoneDatabaseHelper? = null

    companion object {
        fun newInstance(): NewRingtoneFragment {
            return NewRingtoneFragment()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_new_ringtone, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        mDatabase = RingtoneDatabaseHelper.getInstance(context)
        initRecyclerView(view)
//        getDataFromServer()
//        getNewestRingtoneFromRemote()
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(object : Observer<Elements> {
//                    override fun onSubscribe(d: Disposable) {
//
//                    }
//
//                    override fun onError(e: Throwable) {
//                        e.printStackTrace()
//                    }
//
//                    override fun onNext(t: Elements) {
//                        println("Load next.......")
//                        if (t.size > 0) {
//                            mDatabase?.deleteAllRecords()
//                            for (query in t) {
//                                val ringtoneName = query.select("b[itemprop]").text()
//                                val ringtoneAuthor = query.select("a[style][title][href]").text()
//                                val ringtoneLink = query.select("span").attr("data-link")
//                                mDatabase?.addNewestRingtone(
//                                        Ringtone(0, ringtoneName,
//                                                ringtoneAuthor,
//                                                ringtoneLink)
//                                )
//                            }
//                        }
//                    }
//
//                    override fun onComplete() {
//                        mDatabase?.getNewestRingtone()?.let {
//                            mAdapter.setData(it)
//                        }
//                        println("Load completes.......")
//                    }
//                })
    }

    private fun initRecyclerView(view: View) {
        val layoutManager = LinearLayoutManager(context, LinearLayout.VERTICAL, false)
        mRecyclerView = view.findViewById(R.id.recycler_main_newest)
        mRecyclerView.layoutManager = layoutManager
        mAdapter = NewRingtoneAdapter()
        mRecyclerView.adapter = mAdapter
    }

    private fun getDataFromServer() {
//        var doc: Document
//        var newElement: Elements? = null
        Thread(Runnable {
            kotlin.run {
                try {
                    getNewestRingtoneFromRemote()
                            .subscribeOn(Schedulers.newThread())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(object : Observer<Elements> {
                                override fun onSubscribe(d: Disposable) {

                                }

                                override fun onError(e: Throwable) {
                                    e.printStackTrace()
                                }

                                override fun onNext(t: Elements) {
                                    if (t.size > 0) {
                                        mDatabase?.deleteAllRecords()
                                        for (query in t) {
                                            val ringtoneName = query.select("b[itemprop]").text()
                                            val ringtoneAuthor = query.select("a[style][title][href]").text()
                                            val ringtoneLink = query.select("span").attr("data-link")
                                            mDatabase?.addNewestRingtone(
                                                    Ringtone(0, ringtoneName,
                                                            ringtoneAuthor,
                                                            ringtoneLink)
                                            )
                                        }
                                    }
                                }

                                override fun onComplete() {
                                    mDatabase?.getNewestRingtone()?.let {
                                        mAdapter.setData(it)
                                    }
                                    println("Load completes.......")
                                }
                            })
                } catch (e: Exception) {
                    var newestRingtoneList: MutableList<Ringtone> = mutableListOf()
                    getNewestRingtoneFromLocal()
                            .subscribeOn(Schedulers.newThread())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(object :
                                    Observer<MutableList<Ringtone>> {
                                override fun onComplete() {
                                    mAdapter.setData(newestRingtoneList)
                                }

                                override fun onNext(t: MutableList<Ringtone>) {
                                    newestRingtoneList = t
                                }

                                override fun onSubscribe(d: Disposable) {
                                }

                                override fun onError(e: Throwable) {
                                    e.printStackTrace()
                                }

                            })
                }
            }
        }).start()
    }

    private fun getNewestRingtoneFromLocal(): Observable<MutableList<Ringtone>> {
        return Observable.just(mDatabase?.getNewestRingtone())
    }

    private fun getNewestRingtoneFromRemote(): Observable<Elements> {
        val doc = Jsoup.connect("http://tainhacchuong.net").timeout(1000).get()
        val newElement = doc.select("div.item.trailer")
        return Observable.just(newElement)
    }
}