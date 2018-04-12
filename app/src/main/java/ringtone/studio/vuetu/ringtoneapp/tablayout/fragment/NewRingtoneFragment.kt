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
import org.jsoup.select.Elements
import ringtone.studio.vuetu.ringtoneapp.R
import ringtone.studio.vuetu.ringtoneapp.repository.model.Ringtone
import ringtone.studio.vuetu.ringtoneapp.tablayout.fragment.adapter.NewRingtoneAdapter

/**
 * Created by vungoctuan on 4/11/18.
 */
class NewRingtoneFragment : Fragment() {
    private lateinit var mRecyclerView: RecyclerView
    private lateinit var mAdapter: NewRingtoneAdapter

    companion object {
        fun newInstance(): NewRingtoneFragment {
            return NewRingtoneFragment()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_new_ringtone, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initRecyclerView(view)
        getDataFromServer()
    }

    private fun initRecyclerView(view: View) {
        val layoutManager = LinearLayoutManager(context, LinearLayout.VERTICAL, false)
        layoutManager.stackFromEnd = true
        mRecyclerView = view.findViewById(R.id.recycler_main_newest)
        mRecyclerView.layoutManager = layoutManager
        mAdapter = NewRingtoneAdapter()
//        for (i in 1..50) {
//            mAdapter.addRingtone(Ringtone("Con mua ngang qua", "Son Tung", "link"))
//        }
        mRecyclerView.adapter = mAdapter
    }

    private fun getDataFromServer() {
        Log.d("Fragment: ", "Starting")
        Thread(Runnable {
            kotlin.run {
                try {
                    val doc = Jsoup.connect("http://tainhacchuong.net").get()
                    val newElement = doc.select("div.item.trailer")
                    for (link in newElement) {
//                        val ringtoneName = link.select("b[itemprop]").text()
//                        val ringtoneAuthor = link.select("a[style][title][href]").text()
//                        val ringtoneLink = link.select("span").attr("data-link")
//                        mAdapter.addRingtone(Ringtone(ringtoneName, ringtoneAuthor, ringtoneLink))
                    }
                    Observable.just(newElement)
                            .subscribeOn(Schedulers.newThread())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(object : Observer<Elements> {
                                override fun onSubscribe(d: Disposable) {
                                }

                                override fun onNext(t: Elements) {
                                    for (query in t) {
                                        val ringtoneName = query.select("b[itemprop]").text()
                                        val ringtoneAuthor = query.select("a[style][title][href]").text()
                                        val ringtoneLink = query.select("span").attr("data-link")
                                        Log.d("Fragment name ", ringtoneName)
                                        Log.d("Fragment author ", ringtoneAuthor)
                                        Log.d("Fragment link ", ringtoneLink)
                                        mAdapter.addRingtone(
                                                Ringtone(ringtoneName,
                                                        ringtoneAuthor,
                                                        ringtoneLink))
                                    }
                                }

                                override fun onError(e: Throwable) {
                                }

                                override fun onComplete() {
                                }
                            })
//                            }
                } catch (e: Exception) {
                    e.printStackTrace()
                    Log.d("Fragment: ", "Error")
                }
            }

        }).start()
    }

}