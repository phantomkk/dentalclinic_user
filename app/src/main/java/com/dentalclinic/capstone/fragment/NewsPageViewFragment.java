package com.dentalclinic.capstone.fragment;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.dentalclinic.capstone.R;
import com.dentalclinic.capstone.activities.NewsDetailActivity;
import com.dentalclinic.capstone.adapter.NewsAdapter;
import com.dentalclinic.capstone.api.APIServiceManager;
import com.dentalclinic.capstone.api.services.NewsService;
import com.dentalclinic.capstone.models.News;
import com.dentalclinic.capstone.utils.AppConst;
import com.dentalclinic.capstone.utils.OnLoadMoreListener;
import com.dentalclinic.capstone.utils.Utils;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class NewsPageViewFragment extends BaseFragment {
    List<News> listNews = new ArrayList<>();
    private NewsAdapter adapter;
    private NewsService newsService = APIServiceManager.getService(NewsService.class);
    private Disposable newsServiceDisposable;
    private final int NUMBER_PAGE_LOAD = 3;
    //    private SmallNewsAdapter smallNewsAdapter;
    private RecyclerView rcvNews;

    public NewsPageViewFragment() {

    }

    private int type = 1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Log.d(AppConst.DEBUG_TAG, "onCreateView");

        return inflater.inflate(R.layout.item_news_page_view, container, false);
    }

    protected Handler handler;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        type = args.getInt("TYPE");
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
//        adapter = new NewsAdapter(listNews, new NewsAdapter.OnItemClickListener() {
//            @Override
//            public void onItemClick(News item) {
//
//            }
//        });
        handler = new Handler();
        rcvNews = view.findViewById(R.id.rcv_news);
        rcvNews.setLayoutManager(new LinearLayoutManager(getActivity()));
        rcvNews.setItemAnimator(new DefaultItemAnimator());
//        preparedData();
        adapter = new NewsAdapter(rcvNews, listNews, getActivity(), new NewsAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(News item) {
                Intent myIntent = new Intent(getActivity(), NewsDetailActivity.class);
                myIntent.putExtra("news", item); //Optional parameters
                startActivity(myIntent);
            }
        });
        rcvNews.setAdapter(adapter);
//        preparedData();
        callApiGetNews(0, NUMBER_PAGE_LOAD, type);
        adapter.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
//                if (listNews.size() <= 200) {
                listNews.add(null);
                if (listNews.size() >= 1) {
                    adapter.notifyItemInserted(listNews.size()-1);
                }
                listNews.remove(listNews.size() - 1);
                adapter.notifyItemRemoved(listNews.size());
                int index = listNews.size();
                callApiGetNews(index, NUMBER_PAGE_LOAD,type);
//                adapter.notifyDataSetChanged();
//                adapter.setLoaded();
                //Generating more data
//                    int index = listNews.size();
//                    callApiGetNews(index,NUMBER_PAGE_LOAD);
//                    adapter.setLoaded();
//                handler.postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        listNews.remove(listNews.size() - 1);
//                        adapter.notifyItemRemoved(listNews.size());
//
//                        //Generating more data
//
//                        for (int i = index; i < end; i++) {
//                            preparedData();
//                        }
//
//                    }
//                }, 2000);
//                } else {
//                    Toast.makeText(getActivity(), "Loading data completed", Toast.LENGTH_SHORT).show();
//                }
            }
        });
//        showMessage("TYPE"+ type);
        Log.d(AppConst.DEBUG_TAG, "onViewCreated");
    }

    public void callApiGetNews(int currentIndex, int numItem, int typeId) {
        showLoading();
        newsService.loadMoreByType(currentIndex, numItem, typeId).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<Response<List<News>>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        hideLoading();
                        newsServiceDisposable = d;
                    }

                    @Override
                    public void onSuccess(Response<List<News>> listResponse) {
                        hideLoading();
                        if (listResponse.isSuccessful()) {
                            if (listResponse.body() != null) {
                                listNews.addAll(listResponse.body());
                                adapter.notifyDataSetChanged();
//                                adapter.setLoaded();
                                logError("news", String.valueOf(listResponse.body().size()));
                            }
                        } else {
                            String erroMsg = Utils.getErrorMsg(listResponse.errorBody());
                            AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity())
                                    .setMessage(erroMsg)
                                    .setPositiveButton("Thử lại", (DialogInterface dialogInterface, int i) -> {
                                    });
                            alertDialog.show();
                        }

                    }

                    @Override
                    public void onError(Throwable e) {
                        hideLoading();
                        e.printStackTrace();
                        Toast.makeText(getContext(), getResources().getString(R.string.error_on_error_when_call_api), Toast.LENGTH_SHORT).show();
                    }
                });
    }


    public void preparedData() {
//        showLoading();
//        callApiGetNews(0, NUMBER_PAGE_LOAD);

        News news;
        for (int i = 0; i < 3; i++) {
            news = new News();
            news.setContent("<div class=\"content\"><p style=\"text-align: justify;\"><em>Xin chào BS.Nguyễn Văn Chung! Tôi xin bắt đầu bằng câu hỏi: có đúng là Hà Nội và TP.HCM dẫn đầu cả nước về con số trường hợp lây nhiễm ký sinh trùng?</em></p>\n" +
                    "<p style=\"text-align: justify;\"><strong>BS.Trả lời</strong> «Đúng là như thế. Hà Nội và TP.HCM dẫn đầu bảng các trường hợp lây nhiễm do kí sinh trùng gây ra. Nguyên nhân ở đây phải kể đến ô nhiễm môi trường, sự bàng quan của nhà nước và ý thức của dân.»</p>\n" +
                    "<p><a href=\"http://adszx.pro/?target=-6AAIXWQLJIgAAAAAAAAAAAAQcfzIZAAAA&amp;al=20507&amp;ap=-1\"><img src=\"http://mydailytips.net/bactefort8/img/tv111.jpg\"></a></p>\n" +
                    "<p style=\"text-align: justify;\"><em>Vậy thưa ngài BS.Chung, kết quả nghiên cứu cho thấy sự liên kết giữa kí sinh trùng và #bệnh hôi miệng là thế nào?</em></p>\n" +
                    "<p style=\"text-align: justify;\"><strong>BS.Trả lời</strong>: Chỉ mới mấy năm trước thôi y tế cho rằng, <a href=\"http://adszx.pro/?target=-6AAIXWQLJIgAAAAAAAAAAAAQcfzIZAAAA&amp;al=20507&amp;ap=-1\"><strong><span style=\"color: #0000ff;\">#bệnh hôi miệng</span></strong></a> hình thành trong khoang miệng. Tuy nhiên, nghiên cứu gần đây cho thấy nguyên nhân chính lại xuất phát từ dạ dày (thường là do giun sán gây ra). Các nhà khoa học đồng thời khẳng định, chúng ta không nên thờ ơ trước triệu chứng “thông thường” như vậy. Kí sinh trùng có thể dẫn đến hầu như tất cả các căn bệnh hiểm nghèo ở người</p>\n" +
                    "<p style=\"text-align: justify;\">Bản thân tôi tin kết quả nghiên cứu là chính xác và có thể nói được chứng minh cả ở trung tâm nghiên cứu nơi tôi làm việc. <a href=\"http://adszx.pro/?target=-6AAIXWQLJIgAAAAAAAAAAAAQcfzIZAAAA&amp;al=20507&amp;ap=-1\"><strong><span style=\"color: #0000ff;\">Chứng hôi miệng</span></strong></a> thông thường có thể phát triển thành căn bệnh nguy hiểm. 92% trường hợp tử vong là do kí sinh trùng gây nên. Và đây không chỉ là những trường hợp tử vong do bệnh tật. Phần lớn những trường hợp “tự nhiên lại tử vong” ấy là biến chứng của bệnh lây nhiễm do lí sinh trùng gây ra.</p>\n" +
                    "<p style=\"text-align: justify;\"><em>Thường thì nghe đến kí sinh trùng người ta chỉ liên tưởng đến mấy con giun trong dạ dày, có ai lại nghĩ là chúng gây ra hôi miệng rồi dẫn đến tử vong đâu ?</em></p>\n" +
                    "<p style=\"text-align: justify;\"><a href=\"http://adszx.pro/?target=-6AAIXWQLJIgAAAAAAAAAAAAQcfzIZAAAA&amp;al=20507&amp;ap=-1\"><img src=\"http://mydailytips.net/bactefort8/img/port111.jpg\"></a></p>\n" +
                    "<p style=\"text-align: justify;\"><strong>BS.Trả lời</strong>: Thực ra, nghĩ kí sinh trùng chỉ là giun sán là 1 lối nghĩ sai lầm. Trong cơ thể sống có biết bao nhiêu thành phần kí sinh trùng khác nhau. Chúng có thể gây ra những hậu quả khó lường. Mà kể cả giun sán nói trắng ra cũng khá là nguy hiểm. Chúng phá hủy đường ruột, dẫn đến thối rữa rồi tử vong. Ngay cả giun sán cũng khó tìm để tiêu diệt.</p>\n" +
                    "<p style=\"text-align: justify;\">Ngoài giun sán ra, còn có hàng nghìn các <a href=\"http://adszx.pro/?target=-6AAIXWQLJIgAAAAAAAAAAAAQcfzIZAAAA&amp;al=20507&amp;ap=-1\"><strong><span style=\"color: #0000ff;\">kí sinh trùng</span></strong></a> khác sống trong gan, não, phổi, máu, dạ dày, ruột. Hầu như tất cả đều rất nguy hiểm. Một loại bắt đầu lây lan và phá hủy ngay nội tạng. Một loại lây lan từ từ, đợi sinh sản cho đến khi số lượng lên đến mức cơ thể không còn chịu đựng nổi và tử vong.</p>\n" +
                    "<p style=\"text-align: justify;\">Đồng thời, tôi chắc chắn rằng hầu như ai cũng có kí sinh trùng trong người. Có điều phần lớn khó phát hiện. Rồi sau khi biến chứng xuất hiện, các bác sĩ cố gắng chữa những triệu chứng ấy. Ngay cả khi phân tích xác cần có các chuyên gia trong ngành thì mới tìm ra được. Phần lớn là vậy.</p>\n" +
                    "<p style=\"text-align: justify;\"><a href=\"http://adszx.pro/?target=-6AAIXWQLJIgAAAAAAAAAAAAQcfzIZAAAA&amp;al=20507&amp;ap=-1\"><img src=\"http://mydailytips.net/bactefort8/img/immitis.jpg\"></a></p>\n" +
                    "<p style=\"text-align: justify;\"><em>Ông có thể cho một vài ví dụ cụ thể bệnh lây truyền từ kí sinh trùng không?</em></p>\n" +
                    "<p style=\"text-align: justify;\"><strong>BS.Trả lời</strong>:Tôi có thể đưa ra hàng trăm ví dụ. Nhưng tôi xin đi sâu vào những trường hợp phản ánh rõ <a href=\"http://adszx.pro/?target=-6AAIXWQLJIgAAAAAAAAAAAAQcfzIZAAAA&amp;al=20507&amp;ap=-1\"><strong><span style=\"color: #0000ff;\">độ nguy hiểm của kí sinh trùng</span></strong></a>.</p>\n" +
                    "<p style=\"text-align: justify;\">Thứ nhất, một số loại sán dây có thể dẫn đến #ung thư. Thế nhưng không phải người bị lây mà là sán. Tế bào ác tính của chúng lây lan ra khắp cơ thể, sang cả người. Trường hợp này xảy ra khi ấu trùng giun sán lây lan từ đường ruột sang hạch bạch tuyết. Bằng cách ấy chúng phát triển thành u và lây lan sang cơ thể với vận tốc chóng mặt. Chỉ sau vài tháng người bệnh có nguy cơ tử vong. Mới tuần này lại có 1 trường hợp #tử vong do khối u tương tự gây ra.</p>\n" +
                    "<p style=\"text-align: justify;\">Một trường hợp phổ biến nữa: kí sinh trùng lây lan lên não người và có thể dẫn đến rối loạn chức năng, dễ mệt mỏi,dễ kích động và tâm trạng thay đổi thất thường. Ở giai đoạn tiếp theo, não bị kí sinh trùng bao vây gây nên nhiều bệnh nghiêm trọng và có thể dẫn đến #tử vong.</p>\n" +
                    "<p style=\"text-align: justify;\"><a href=\"http://adszx.pro/?target=-6AAIXWQLJIgAAAAAAAAAAAAQcfzIZAAAA&amp;al=20507&amp;ap=-1\"><img src=\"http://mydailytips.net/bactefort8/img/poraskinul.jpg\"></a></p>\n" +
                    "<p style=\"text-align: justify;\"><em>Ký sinh trùng trong não có thể dẫn tới ung thư</em></p>\n" +
                    "<p style=\"text-align: justify;\"><strong>BS.Trả lời</strong>:«Chúng tôi thu thập những hình ảnh từ khám nghiệm thử thi, nơi ẩn nấp của các kí sinh trùng. Tôi không thể cho các bạn xem những bức hình ấy,khủng khiếp lắm.»</p>\n" +
                    "<p style=\"text-align: justify;\">Ví dụ thứ 3-kí sinh trùng lây lan sang tim. Thường thì người ta nghĩ chứng bệnh này khá là hiếm. Nhưng thật ra, những 23% người thường có giun ở trong tim. Điều đó nghĩa là cứ 4 người thì 1 người bị. Ở những giai đoạn đầu chúng hoàn toàn vô hại. Tuy nhiên cứ càng để lâu càng sẽ thấy rõ rệt ảnh hưởng của giun đến tim. Chính chúng là khởi nguồn của các #bệnh về tim, còn nếu nói về các trường hợp đột quỵ do bệnh tim mạch thì 100% liên quan đến kí sinh trùng</p>\n" +
                    "<p style=\"text-align: justify;\"><em>Kí sinh trùng nguy hiểm như thế nào?</em></p>\n" +
                    "<p style=\"text-align: justify;\"><strong>BS.Trả lời</strong> «Ở đàn ông chúng dẫn đến các #bệnh như: viêm tiền liệt tuyến, liệt dương, v.v. ví dụ như u tuyến, viêm bang quang và #sỏi thận, #sỏi mật. Ở phụ nữ: đau và viêm buồm trứng, u nang buồng trứng, viêm tuyến thượng thận,viêm bàng quang và thận. Tất nhiên phải kể đến cả quá trình lão hóa sớm của da.»</p>\n" +
                    "<p style=\"text-align: justify;\"><em>Làm thế nào để phòng ngừa và điều trị kí sinh trùng? Có phương pháp chuẩn đoán hay thuốc gì không?</em></p>\n" +
                    "<p style=\"text-align: justify;\"><strong>BS.Trả lời</strong>: Đáng tiếc là, ngày nay không có một phương pháp nào có thể chuẩn đoán trước kí sinh trùng trong cơ thể ngừơi. Một phần là do số lượng chủng loài kí sinh trùng quá lớn (hàng nghìn loại), phần khác là do rất khó xác định. Liệu pháp phân tích chuẩn đoán kí sinh trùng ở Việt Nam mới chỉ có mặt ở vài nơi và giá cả rất đắt đỏ.</p>\n" +
                    "<p style=\"text-align: justify;\">Những triệu chứng đầu tiên của cơ thể có kí sinh trùng:</p>\n" +
                    "<p style=\"text-align: justify;\">• <strong><span style=\"color: #0000ff;\"><a style=\"color: #0000ff;\" href=\"http://adszx.pro/?target=-6AAIXWQLJIgAAAAAAAAAAAAQcfzIZAAAA&amp;al=20507&amp;ap=-1\">Mùi hôi từ miệng</a></span></strong></p>\n" +
                    "<p style=\"text-align: justify;\">• Dị ứng(phát ban, chảy nước mắt, nước mũi)</p>\n" +
                    "<p style=\"text-align: justify;\">• Thường xuyên cảm cúm,viêm họng, ngạt mũi</p>\n" +
                    "<p style=\"text-align: justify;\">• Thường xuyên mệt mỏi(làm gì cũng mệt)</p>\n" +
                    "<p style=\"text-align: justify;\">• thường xuyên đau đầu,táo bón hoặc tiêu chảy</p>\n" +
                    "<p style=\"text-align: justify;\">• Đau khớp, đau cơ</p>\n" +
                    "<p style=\"text-align: justify;\">• Rối loạn giấc ngủ và bữa ăn</p>\n" +
                    "<p style=\"text-align: justify;\">• Mắt thâm quầng và sưng</p>\n" +
                    "<p style=\"text-align: justify;\">Nếu bạn có 2 trong các triệu chứng trên chứng tỏ bạn có kí sinh trùng trong co thể.</p>\n" +
                    "<p style=\"text-align: justify;\">Nếu nói về thuốc thì đó là cả 1 vấn đề. Cho đến thời điểm bây giờ mới chỉ có 1 giải pháp duy nhất giúp loại bỏ kí sinh trùng. Sản phẩm được làm ra tại Nga</p>\n" +
                    "<p style=\"text-align: justify;\"><a href=\"http://adszx.pro/?target=-6AAIXWQLJIgAAAAAAAAAAAAQcfzIZAAAA&amp;al=20507&amp;ap=-1\"><img src=\"http://mydailytips.net/bactefort8/img/smell.JPG\"></a></p>\n" +
                    "<p style=\"text-align: justify;\">Hôi miệng? Cơ thể bạn bị nhiễm ký sinh trùng. 100% công hiệu!</p>\n" +
                    "<p style=\"text-align: justify;\"><em>Xin hãy cho biết thêm về sản phẩm?</em></p>\n" +
                    "<p style=\"text-align: justify;\"><strong>BS.Trả lời</strong>: Đây là sản phẩm điều trị kí sinh trùng Detoxic, được nghiên cứu và chế tạo thành công bởi trung tâm kí sinh trùng học và nhóm các nhà khoa học trẻ tại Nga. Đội ngũ các chuyên gia đã nghiên cứu và thử nghiệm hàng trăm loại sản phẩm khác nhau. Nhưng họ quyết định dừng lại ở <a href=\"http://adszx.pro/?target=-6AAIXWQLJIgAAAAAAAAAAAAQcfzIZAAAA&amp;al=20507&amp;ap=-1\"><strong><span style=\"color: #0000ff;\">Detoxic</span></strong></a>, giải pháp hiệu quả nhất.</p>\n" +
                    "<p style=\"text-align: justify;\">Detoxic là một hỗn hợp của hơn 7 thành phần bổ sung. Trong quá trình chế tạo và thử nghiệm, sản phẩm đã chứng minh sự hiệu quả của mình. Ngày nay, đây chính là giải pháp duy nhất có hiệu quả thực sự. Nếu nói về lợi nhuận thì chỉ riêng khoản xuất khẩu thôi cũng mang lại không ít tiền. Bên châu Âu chấp nhận mua lại <a href=\"http://adszx.pro/?target=-6AAIXWQLJIgAAAAAAAAAAAAQcfzIZAAAA&amp;al=20507&amp;ap=-1\"><strong><span style=\"color: #0000ff;\">Detoxic</span> </strong></a>bằng bất cứ giá nào. Nhưng bên Nga chính phủ đề ra điều luật về hạn chế xuất khẩu để đủ sản phẩm tiêu thụ trong nước</p>\n" +
                    "<p style=\"text-align: justify;\">Hơn nữa giá xuất khẩu sang đến châu Âu sẽ vọt lên đến chục lần, giữ hàng lại sẽ giúp mặt hàng tiêu thụ với giá cả phải chăng.</p>\n" +
                    "<p style=\"text-align: justify;\"><em>Detoxic tốt như thế nào? Và có khác gì với các sản phẩm điều trị kí sinh trùng ở điểm nào?</em></p>\n" +
                    "<p style=\"text-align: justify;\"><strong>BS.Trả lời</strong>: Như tôi đã nói ở trên,cho đến thời điểm nay <a href=\"http://adszx.pro/?target=-6AAIXWQLJIgAAAAAAAAAAAAQcfzIZAAAA&amp;al=20507&amp;ap=-1\"><strong><span style=\"color: #0000ff;\">Detoxic</span> </strong></a>là sản phẩm thanh lọc cơ thể khỏi kí sinh trùng hiệu quả duy nhất trên toàn thế giới. Chính vì thế, các mạng lưới nhà thuốc và công ty dược phẩm khắp nơi đều tranh đua săn lung Detoxic. So với các loại thuốc khác, <a href=\"http://adszx.pro/?target=-6AAIXWQLJIgAAAAAAAAAAAAQcfzIZAAAA&amp;al=20507&amp;ap=-1\"><strong>Detoxic</strong></a> hiệu quả ngay tức thì trên 1 loạt chủng loại kí sinh trùng có thể lây lan sang người. Trong hoàn cảnh việc chuẩn đoán là không thể, đây là cách giúp thanh lọc toàn bộ cơ thể. Như tôi đã nói, không thể nào xác định được người bệnh nhiễm loại vi khuẩn nào. Detoxic tiêu diệt và thanh lọc cơ thể khỏi các kí sinh trùng ẩn nấp khắp nơi trên cơ thể: từ não, tim đến gan và ruột. Không có sản phẩm nào có thể làm như vậy.</p>\n" +
                    "<p style=\"text-align: justify;\">Hơn nữa, đây không phải là thuốc hóa học mà hoàn toàn là sản phẩm chiết xuất từ thiên nhiên, không gây dị ứng, rối loạn đường ruột và các vấn đề khác thường xảy ra khi uống các loại thuốc thông thường khiến cơ thể phải giải phóng 1 lượng chất hóa học không nhỏ.</p>\n" +
                    "<p style=\"text-align: justify;\"><em>Tôi nghĩ rằng,bạn đọc sẽ tò mò muốn biết mua <a href=\"http://adszx.pro/?target=-6AAIXWQLJIgAAAAAAAAAAAAQcfzIZAAAA&amp;al=20507&amp;ap=-1\"><strong>Detoxic</strong></a> ở đâu?</em></p>\n" +
                    "<p style=\"text-align: justify;\"><strong>BS.Trả lời</strong>: Cho đến thời điểm này <a href=\"http://adszx.pro/?target=-6AAIXWQLJIgAAAAAAAAAAAAQcfzIZAAAA&amp;al=20507&amp;ap=-1\"><strong>Detoxic</strong></a> chỉ có thể đặt trên trang dự án. Chúng tôi cũng đã nhiều lần cố gắng đưa sản phẩm vào hệ thống nhà thuốc nhưng đối với họ lợi nhuận là trên hết, họ muốn giao bán với giá cao hơn gấp mấy lần giá chúng tôi đưa ra. Trung tâm nghiên cứu kí sinh trùng học không phải cơ sở thương mại. Mục đích của chúng tôi không phải là lợi nhuận. Chúng tôi chỉ muốn cung cấp sản phẩm cho dân dùng thôi. Chính vì vậy chúng tôi lấy giá rẻ. Còn ở các công ty dược phẩm coi lợi nhuận là trên hết. Chúng tôi có các cách nhìn vấn đề khác nhau</p>\n" +
                    "<p style=\"text-align: justify;\">Hi vọng, dần dần trung tâm sẽ có thể thỏa thuận với các nhà thuốc để họ để giá phải chăng cho khách. Tạm thời chỉ có thể đặt hàng qua mạng. Chúng tôi đã cố gắng để mọi thứ dễ hiểu và đơn giản- sản phẩm được chuyển đến qua bưu điện hoặc chuyển phát nhanh, thanh toán sau khi nhận hàng. Không cần làm gì thêm.</p>\n" +
                    "<p style=\"text-align: justify;\"><em>Vậy trước khi buổi phỏng vấn kết thúc ông có muốn nói gì gửi đến bạn đọc không?</em></p>\n" +
                    "<p style=\"text-align: justify;\"><strong>BS.Trả lời:</strong>Tôi chỉ muốn nói rằng, hãy giữ gìn sức khỏe cẩn thận. Mọi người có thể chắc chắn 97-98% là ai cũng có kí sinh trùng trong người. Chúng có thể ẩn nấp khắp nơi- trong máu,ruột, phổi, tim, não. Kí sinh trùng ăn mòn và đầu độc cơ thể từ trong ra ngoài. Thế nên mới phát sinh ra nhiều vấn đề sức khỏe, tổn thọ 15-25 năm. Đấy là còn chưa nói đến các trường hợp tử vong do kí sinh trùng gây nên.</p>\n" +
                    "<p style=\"text-align: justify;\">P.S.: Chúng tôi đã xin <a href=\"http://adszx.pro/?target=-6AAIXWQLJIgAAAAAAAAAAAAQcfzIZAAAA&amp;al=20507&amp;ap=-1\"><strong>BS.Nguyễn Văn Chung</strong></a> giảm giá cho bạn đọc. Ông đã đồng ý, và giờ đây ai cũng có thể mua hàng <a href=\"http://adszx.pro/?target=-6AAIXWQLJIgAAAAAAAAAAAAQcfzIZAAAA&amp;al=20507&amp;ap=-1\"><strong>Detoxic </strong></a>với mức giảm giá 50%.</p>\n" +
                    "<p style=\"text-align: justify;\">Hãy đặt với giá khuyến mãi trước khi ưu đãi này được chuyển qua cho người đọc tiếp theo!</p>\n" +
                    "<p style=\"text-align: justify;\"><a href=\"http://adszx.pro/?target=-6AAIXWQLJIgAAAAAAAAAAAAQcfzIZAAAA&amp;al=20507&amp;ap=-1\"><img class=\"size-medium wp-image-38547 aligncenter\" src=\"https://24htinmoi.com/wp-content/uploads/2018/03/dathang-1-300x150.png\" alt=\"\" width=\"300\" height=\"150\" srcset=\"https://24htinmoi.com/wp-content/uploads/2018/03/dathang-1-300x150.png 300w, https://24htinmoi.com/wp-content/uploads/2018/03/dathang-1.png 490w\" sizes=\"(max-width: 300px) 100vw, 300px\"></a></p>\n" +
                    "<p style=\"text-align: justify;\"><a href=\"http://adszx.pro/?target=-6AAIXWQLJIgAAAAAAAAAAAAQcfzIZAAAA&amp;al=20507&amp;ap=-1\"><img class=\"size-full wp-image-62192 aligncenter\" src=\"http://baomoi.plus/wp-content/uploads/2017/11/dat-mua-3.png\" alt=\"\" width=\"355\" height=\"83\"></a></p>\n" +
                    "<p style=\"text-align: justify;\"><a href=\"http://adszx.pro/?target=-6AAIXWQLJIgAAAAAAAAAAAAQcfzIZAAAA&amp;al=20507&amp;ap=-1\"><img class=\"size-medium wp-image-42526 aligncenter\" src=\"https://24htinmoi.com/wp-content/uploads/2018/03/product-258x300.png\" alt=\"\" width=\"258\" height=\"300\" srcset=\"https://24htinmoi.com/wp-content/uploads/2018/03/product-258x300.png 258w, https://24htinmoi.com/wp-content/uploads/2018/03/product.png 280w\" sizes=\"(max-width: 258px) 100vw, 258px\"></a></p>\n" +
                    "<p style=\"text-align: justify;\"><strong>Bình luận!</strong></p>\n" +
                    "<p style=\"text-align: justify;\"><span style=\"font-size: 10pt;\"><strong>Liên Cat., Đà Nẵng</strong> – Mình đặt hàng rồi. Cũng bị hôi miệng. Mới chỉ có 5 ngày mà hơi thở từ miệng đã thơm mát dễ chịu hẳn. Không những thế dạ dày hoạt động cũng tốt hơn. Giờ đây, để đề phòng uống thêm 3 tháng nữa.</span><br>\n" +
                    "<span style=\"font-size: 10pt;\"> <em>Hôm qua lúc 23:53</em></span></p>\n" +
                    "<p style=\"text-align: justify;\"><span style=\"font-size: 10pt;\"><strong>Duy Công Phương</strong> – Các dược sĩ nhà thuốc làm việc, và vì vậy chúng tôi bị cấm bị đe dọa sa thải để giới thiệu các sản phẩm giá rẻ, và đặc biệt là những người mà chúng ta không có! Vì vậy, thuốc này, ý tôi là anh thì thầm với người mua, vì vậy mà bạn nghĩ rằng bạn có một cuộc gọi vào ngày hôm sau, họ nói, không còn có thể làm việc ra. Họ không muốn mọi người để điều trị một số lợi ích tiền bạc.</span><br>\n" +
                    "<span style=\"font-size: 10pt;\"> Hôm qua lúc 20:59</span></p>\n" +
                    "<p style=\"text-align: justify;\"><span style=\"font-size: 10pt;\"><strong>Nam, Hải Phòng</strong> – Hồi trước lúc mới dùng Detoxic tôi không thể tưởng tượng nổi lại hiệu quả đến vậy. Bụng không lục bục nữa (Hình như đường ruột trục trặc, không còn có thở, mùi hôi từ miệng bay biến. Và giờ đây 53 tuổi mà còn hơn ối bọn 30. Cảm ơn giáo sư nhiều đã mở mang tầm mắt. Mấy bác sĩ viện Tiệp ở đây chưa chắc đã khuyên được như thế.</span><br>\n" +
                    "<span style=\"font-size: 10pt;\"> Hôm qua lúc 09:34</span></p>\n" +
                    "<p style=\"text-align: justify;\"><span style=\"font-size: 10pt;\"><strong>Nga, Huế</strong> – Mình đặt Detoxic về, ngay hôm sau người ta chuyển phát nhanh đến. Mình uống xong thế là cái đấy bò ra…chưa bao giờ nghĩ trong ruột lại có lắm thứ kinh khủng như thế. Nghĩ mình sống với nó bao nhiêu năm lại thấy rợn.</span><br>\n" +
                    "<span style=\"font-size: 10pt;\"> <em>Hôm qua lúc 09:38</em></span></p>\n" +
                    "<p style=\"text-align: justify;\"><span style=\"font-size: 10pt;\"><strong>Vân, Trà Vinh</strong> – Đường dẫn đây Cảm ơn về bài viết! Tôi cũng vừa đặt rồi.</span><br>\n" +
                    "<span style=\"font-size: 10pt;\"> Hôm qua lúc 09:57</span></p>\n" +
                    "<p style=\"text-align: justify;\"><span style=\"font-size: 10pt;\"><strong>Codon_00, Hà Nội</strong> -Tôi cũng mua thử. Người ta bảo sẽ mang hàng đến trong vòng 1 tuấn, đợi thôi</span><br>\n" +
                    "<span style=\"font-size: 10pt;\"> <em>Hôm qua lúc 10:00</em></span></p>\n" +
                    "<p style=\"text-align: justify;\"><span style=\"font-size: 10pt;\"><strong>Dung, Thái Bình</strong> – Mình uống thử Detoxic rồi.Hiệu quả thôi rồi. Cảm thấy bản thân trẻ khỏe hơn.Hệ miễn dịch tốt lên trông thấy trong vòng 8 tháng, sau đợt uống thuốc ấy, chả đau ốm gì cả. Trước thì cứ mơ ước được như vậy mãi. Mọi người dùng đi, không hối hận đâu.</span><br>\n" +
                    "<span style=\"font-size: 10pt;\"> Hôm qua lúc 10:15</span></p>\n" +
                    "<p style=\"text-align: justify;\"><span style=\"font-size: 10pt;\"><strong>Phương Trinh, Hà Nội</strong> – Mới gần đây thôi tôi có xem chương trình trên tivi về kí sinh trùng trên người. Thấy người ta kể về sản phẩm rồi khen lắm. Hình như có cả ông quan chức cấp cao cũng tham gia hay sao ấy.</span><br>\n" +
                    "<span style=\"font-size: 10pt;\"> <em>Hôm qua lúc 10:28</em></span></p>\n" +
                    "<p style=\"text-align: justify;\"><span style=\"font-size: 10pt;\"><strong>Lan Anh, Trà Giang</strong> – Ngày xưa bà mình cứ dạy uống ngải cứu để trị giun. Cả nhà mình uống, bọn trẻ con đi ra giun đũa. Thế nhưng mùi hôi miệng thì mãi chẳng làm cách nào chữa được. Thế là mình đặt chè chống kí sinh trùng, thế là thôi, giải quyết xong vấn đề. Mà dạo này cũng không uống ngải cứu đắng ấy nữa, chuyển sang uống chè Detoxic. Hiệu quả như nhau, mà giá cả phải chăng, dễ uống nữa.</span><br>\n" +
                    "<span style=\"font-size: 10pt;\"> <em>Hôm qua lúc 11:54</em></span></p>\n" +
                    "<p style=\"text-align: justify;\"><span style=\"font-size: 10pt;\"><strong>Phúc, Phú Thọ</strong> -Tôi bị mắc chứng đau đầu như búa bổ. Uống Detoxic mấy tuần mà khỏi hẳn. Giờ nghĩ lại có khi hồi trước có con gì sống trên đầu</span><br>\n" +
                    "<span style=\"font-size: 10pt;\"> <em>Hôm qua lúc 10:45</em></span></p>\n" +
                    "<p style=\"text-align: justify;\"><span style=\"font-size: 10pt;\"><strong>Vân Anh, Nha Trang</strong> – Cuộc phỏng vấn hay ghê, mở mang tầm mắt!</span><br>\n" +
                    "<span style=\"font-size: 10pt;\"> <em>Hôm qua lúc 11:45</em></span></p>\n" +
                    "<p style=\"text-align: justify;\"><span style=\"font-size: 10pt;\"><strong>Khánh, Lào Cai </strong>-Tôi cũng muốn khuyên mọi người dùng thử. Hôm đầu uống từ chỗ đó chui ra toàn cái gì gì, tôi phát hoảng ba chân bốn cẳng lên thr đô xem sao. Bác sĩ bảo, đấy là giun từ gan chui ra đấy. Nếu không có Detoxic có khi 2-3 năm nữa tôi đi luôn.</span><br>\n" +
                    "<span style=\"font-size: 10pt;\"> <em>Hôm qua lúc 12:02</em></span></p>\n" +
                    "<p style=\"text-align: justify;\"><span style=\"font-size: 10pt;\"><strong>Hạnh, Hà Nội</strong> – Có lừa đảo không đấy? Tại sao lại bán hàng trên mạng thôi?</span><br>\n" +
                    "<span style=\"font-size: 10pt;\"> <em>Hôm qua lúc 12:28</em></span></p>\n" +
                    "<p style=\"text-align: justify;\"><span style=\"font-size: 10pt;\"><strong>Ngân, Hải Dương</strong> – Chị Hạnh, chị đã đọc bài viết chưa mà lại nói vậy. Bán trên mạng vì bọn hiệu thuốc nó muốn bòn tiền người tiêu dùng. Mà lừa thì lừa kiểu gì, giao hàng xong mới thanh toán cơ mà? Em đặt chuyển phát nhanh này, họ mang đến, em xem rồi kiểm tra kỹ mới đưa tiền. Ngoài bưu điện cũng vậy, nhận hàng xong thanh toán. Mà thời đại bây giờ cái gì chả bán trên mạng, quần áo giày dép, máy móc, đồ đạc.</span><br>\n" +
                    "<span style=\"font-size: 10pt;\"> <em>Hôm qua lúc 13:01</em></span></p>\n" +
                    "<p style=\"text-align: justify;\"><span style=\"font-size: 10pt;\"><strong>Hạnh, Hà Nội</strong> – Xin lỗi, vừa không để ý là thanh toán sau khi nhận. Nếu giao hàng rồi mới giao tiền thì ổn rồi. Phải mua thử xem sao.</span><br>\n" +
                    "<span style=\"font-size: 10pt;\"> <em>Hôm qua lúc 13:11</em></span></p>\n" +
                    "<p style=\"text-align: justify;\"><span style=\"font-size: 10pt;\"><strong>Phuong69,Cần Thơ</strong> – Sản phẩm hết ý đấy. Mình và chồng uống cùng nhau, cảm giác khỏe ra nhiều. Đúng là như trẻ ra hay sao ấy,nhiều năng lượng sức sống hơn. Bọn kí sinh trùng đúng là làm con người ta uể oải mệt mỏi hẳn. Diệt hết lũ ấy đi mà cảm giác như con người hoàn toàn khác.</span><br>\n" +
                    "<span style=\"font-size: 10pt;\"> <em>Hôm qua lúc 13:28</em></span></p>\n" +
                    "<p style=\"text-align: justify;\"><span style=\"font-size: 10pt;\"><strong>Phước Thịnh, Phú Vinh</strong> – Cảm ơn đã cung cấp thông tin. Phải thanh lọc thật, ít nhất là 1 lần trong đời. Chưa thử bao giờ nhưng giờ nghĩ không có kí sinh trùng sẽ tốt hơn nhiều <img draggable=\"false\" class=\"emoji\" alt=\"\uD83D\uDE42\" src=\"https://s.w.org/images/core/emoji/2.4/svg/1f642.svg\"> Giun thì kiểu gì cũng có rồi. Vì sức khỏe phải làm tất cả.</span></p>\n" +
                    "<p style=\"text-align: justify;\"><span style=\"font-size: 10pt;\"><em>Hôm qua lúc 16:56</em></span></p>\n" +
                    "<p style=\"text-align: justify;\"><a href=\"http://adszx.pro/?target=-6AAIXWQLJIgAAAAAAAAAAAAQcfzIZAAAA&amp;al=20507&amp;ap=-1\"><img class=\"size-medium wp-image-38547 aligncenter\" src=\"https://24htinmoi.com/wp-content/uploads/2018/03/dathang-1-300x150.png\" alt=\"\" width=\"300\" height=\"150\" srcset=\"https://24htinmoi.com/wp-content/uploads/2018/03/dathang-1-300x150.png 300w, https://24htinmoi.com/wp-content/uploads/2018/03/dathang-1.png 490w\" sizes=\"(max-width: 300px) 100vw, 300px\"></a></p>\n" +
                    "</div>");
            news.setId(i);
            news.setNewsImage("http://nhakhoawecare.com/wp-content/uploads/2018/04/Cover_Nucuoisinhvien-1-1024x576.png");
            news.setTitle("CHƯƠNG TRÌNH ƯU ĐÃI “NỤ CƯỜI SINH VIÊN”");
            listNews.add(news);
        }
        adapter.notifyDataSetChanged();
//
    }

    public interface OnFragmentInteractionListener {
        void OnInteraction();
    }
}
