import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.androidrealm.bookhub.Adapter.PrizeAdapter
import com.androidrealm.bookhub.Models.Prize
import com.androidrealm.bookhub.R

class PrizeFragment(listPrize: Any?) : Fragment() {
    private lateinit var prizeRW: RecyclerView
    companion object {
        fun newInstance
                    (listPrize: ArrayList<Prize>): PrizeFragment
        {
            val fragment= PrizeFragment(listPrize)
            val bundle = Bundle()
            bundle.putSerializable("listPrize", listPrize)
            fragment.setArguments(bundle)
            return fragment
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var view=inflater.inflate(R.layout.fragment_prize_list, container, false)
        prizeRW=view.findViewById(R.id.prizeRW)
        // set the custom adapter to the RecyclerView

        var listPrize=requireArguments().getSerializable(
            "listPrize"
        ) as ArrayList<Prize>

        prizeRW.addItemDecoration(
            DividerItemDecoration(
                prizeRW.getContext(),
                DividerItemDecoration.VERTICAL
            )
        )

        var adapter= PrizeAdapter(listPrize)
        prizeRW.adapter=adapter

        prizeRW.layoutManager = LinearLayoutManager(activity)

        return view
    }

}