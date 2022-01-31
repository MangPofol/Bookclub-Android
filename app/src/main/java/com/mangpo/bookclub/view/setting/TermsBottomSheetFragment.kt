package com.mangpo.bookclub.view.setting

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.mangpo.bookclub.R
import com.mangpo.bookclub.databinding.FragmentTermsBottomSheetBinding

class TermsBottomSheetFragment(private val type: String) : BottomSheetDialogFragment() {
    private lateinit var binding: FragmentTermsBottomSheetBinding

    companion object {
        fun newInstance(type: String): TermsBottomSheetFragment = TermsBottomSheetFragment(type)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("TermsBottomSheetFragment", "onCreate")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d("TermsBottomSheetFragment", "onCreateView")
        binding = FragmentTermsBottomSheetBinding.inflate(inflater, container, false)

        //type 에 따라 제목과 본문을 다르게 바꾼다.
        when (type) {
            "service" -> {
                binding.titleTv.text = getString(R.string.terms_of_service)
                binding.contentTv.text = getString(R.string.terms_of_service_content)
            }
            "privacy" -> {
                binding.titleTv.text = getString(R.string.privacy_policy)
                binding.contentTv.text = "<Ourpage>(이하 'Ourpage')은(는) 「개인정보 보호법」 제30조에 따라 정보주체의 개인정보를 보호하고 이와 관련한 고충을 신속하고 원활하게 처리할 수 있도록 하기 위하여 다음과 같이 개인정보 처리방침을 수립·공개합니다.\n" +
                        "\n" +
                        "○ 이 개인정보처리방침은 2022년 1월 31부터 적용됩니다.\n" +
                        "\n" +
                        "\n" +
                        "제1조(개인정보의 처리 목적)\n" +
                        "\n" +
                        "< Ourpage >(이하 'Ourpage')은(는) 다음의 목적을 위하여 개인정보를 처리합니다. 처리하고 있는 개인정보는 다음의 목적 이외의 용도로는 이용되지 않으며 이용 목적이 변경되는 경우에는 「개인정보 보호법」 제18조에 따라 별도의 동의를 받는 등 필요한 조치를 이행할 예정입니다.\n" +
                        "\n" +
                        "1. 홈페이지 회원가입 및 관리\n" +
                        "\n" +
                        "회원 가입의사 확인, 회원제 서비스 제공에 따른 본인 식별·인증, 회원자격 유지·관리, 서비스 부정이용 방지 목적으로 개인정보를 처리합니다.\n" +
                        "\n" +
                        "\n" +
                        "제2조(개인정보의 처리 및 보유 기간)\n" +
                        "\n" +
                        "① < Ourpage >은(는) 법령에 따른 개인정보 보유·이용기간 또는 정보주체로부터 개인정보를 수집 시에 동의받은 개인정보 보유·이용기간 내에서 개인정보를 처리·보유합니다.\n" +
                        "\n" +
                        "② 각각의 개인정보 처리 및 보유 기간은 다음과 같습니다.\n" +
                        "\n" +
                        "1.<홈페이지 회원가입 및 관리>\n" +
                        "<홈페이지 회원가입 및 관리>와 관련한 개인정보는 수집.이용에 관한 동의일로부터<탈퇴 후 1년>까지 위 이용목적을 위하여 보유. 이용됩니다.\n" +
                        "\n" +
                        "제3조(개인정보처리 위탁)\n" +
                        "\n" +
                        "① < Ourpage >은(는) 원활한 개인정보 업무처리를 위하여 다음과 같이 개인정보 처리업무를 위탁하고 있습니다.\n" +
                        "\n" +
                        "1. < Amazon Web Service >\n" +
                        "위탁받는 자 (수탁자) : Amazon Web Service\n" +
                        "위탁하는 업무의 내용 : 데이터 보관\n" +
                        "위탁기간 : 탈퇴 후 1\n" +
                        "② < Ourpage >은(는) 위탁계약 체결시 「개인정보 보호법」 제26조에 따라 위탁업무 수행목적 외 개인정보 처리금지, 기술적․관리적 보호조치, 재위탁 제한, 수탁자에 대한 관리․감독, 손해배상 등 책임에 관한 사항을 계약서 등 문서에 명시하고, 수탁자가 개인정보를 안전하게 처리하는지를 감독하고 있습니다.\n" +
                        "\n" +
                        "③ 위탁업무의 내용이나 수탁자가 변경될 경우에는 지체없이 본 개인정보 처리방침을 통하여 공개하도록 하겠습니다.\n" +
                        "\n" +
                        "\n" +
                        "\n" +
                        "제4조(정보주체와 법정대리인의 권리·의무 및 그 행사방법)\n" +
                        "\n" +
                        "\n" +
                        "\n" +
                        "① 정보주체는 Ourpage에 대해 언제든지 개인정보 열람·정정·삭제·처리정지 요구 등의 권리를 행사할 수 있습니다.\n" +
                        "\n" +
                        "② 제1항에 따른 권리 행사는Ourpage에 대해 「개인정보 보호법」 시행령 제41조제1항에 따라 서면, 전자우편, 모사전송(FAX) 등을 통하여 하실 수 있으며 Ourpage은(는) 이에 대해 지체 없이 조치하겠습니다.\n" +
                        "\n" +
                        "③ 제1항에 따른 권리 행사는 정보주체의 법정대리인이나 위임을 받은 자 등 대리인을 통하여 하실 수 있습니다.이 경우 “개인정보 처리 방법에 관한 고시(제2020-7호)” 별지 제11호 서식에 따른 위임장을 제출하셔야 합니다.\n" +
                        "\n" +
                        "④ 개인정보 열람 및 처리정지 요구는 「개인정보 보호법」 제35조 제4항, 제37조 제2항에 의하여 정보주체의 권리가 제한 될 수 있습니다.\n" +
                        "\n" +
                        "⑤ 개인정보의 정정 및 삭제 요구는 다른 법령에서 그 개인정보가 수집 대상으로 명시되어 있는 경우에는 그 삭제를 요구할 수 없습니다.\n" +
                        "\n" +
                        "⑥ Ourpage은(는) 정보주체 권리에 따른 열람의 요구, 정정·삭제의 요구, 처리정지의 요구 시 열람 등 요구를 한 자가 본인이거나 정당한 대리인인지를 확인합니다.\n" +
                        "\n" +
                        "\n" +
                        "\n" +
                        "제5조(처리하는 개인정보의 항목 작성)\n" +
                        "\n" +
                        "① < Ourpage >은(는) 다음의 개인정보 항목을 처리하고 있습니다.\n" +
                        "\n" +
                        "1< 홈페이지 회원가입 및 관리 >\n" +
                        "필수항목 : 이메일, 비밀번호, 성별, 생년월일\n" +
                        "선택항목 :\n" +
                        "\n" +
                        "\n" +
                        "제6조(개인정보의 파기)\n" +
                        "\n" +
                        "\n" +
                        "① < Ourpage > 은(는) 개인정보 보유기간의 경과, 처리목적 달성 등 개인정보가 불필요하게 되었을 때에는 지체없이 해당 개인정보를 파기합니다.\n" +
                        "\n" +
                        "② 정보주체로부터 동의받은 개인정보 보유기간이 경과하거나 처리목적이 달성되었음에도 불구하고 다른 법령에 따라 개인정보를 계속 보존하여야 하는 경우에는, 해당 개인정보를 별도의 데이터베이스(DB)로 옮기거나 보관장소를 달리하여 보존합니다.\n" +
                        "  보존하는 개인정보 항목 : 이메일, 비밀번호, 성별, 생년월일\n" +
                        "\n" +
                        "③ 개인정보 파기의 절차 및 방법은 다음과 같습니다.\n" +
                        "1. 파기절차\n" +
                        "< Ourpage > 은(는) 파기 사유가 발생한 개인정보를 선정하고, < Ourpage > 의 개인정보 보호책임자의 승인을 받아 개인정보를 파기합니다.\n" +
                        "\n" +
                        "2. 파기방법\n" +
                        "\n" +
                        "전자적 파일 형태의 정보는 기록을 재생할 수 없는 기술적 방법을 사용합니다\n" +
                        "\n" +
                        "\n" +
                        "\n" +
                        "제7조(개인정보의 안전성 확보 조치)\n" +
                        "\n" +
                        "< Ourpage >은(는) 개인정보의 안전성 확보를 위해 다음과 같은 조치를 취하고 있습니다.\n" +
                        "\n" +
                        "1. 개인정보에 대한 접근 제한\n" +
                        "개인정보를 처리하는 데이터베이스시스템에 대한 접근권한의 부여,변경,말소를 통하여 개인정보에 대한 접근통제를 위하여 필요한 조치를 하고 있으며 침입차단시스템을 이용하여 외부로부터의 무단 접근을 통제하고 있습니다.\n" +
                        "\n" +
                        "\n" +
                        "\n" +
                        "\n" +
                        "제8조(개인정보 자동 수집 장치의 설치•운영 및 거부에 관한 사항)\n" +
                        "\n" +
                        "\n" +
                        "\n" +
                        "Ourpage 은(는) 정보주체의 이용정보를 저장하고 수시로 불러오는 ‘쿠키(cookie)’를 사용하지 않습니다.\n" +
                        "\n" +
                        "제9조 (개인정보 보호책임자)\n" +
                        "\n" +
                        "① Ourpage 은(는) 개인정보 처리에 관한 업무를 총괄해서 책임지고, 개인정보 처리와 관련한 정보주체의 불만처리 및 피해구제 등을 위하여 아래와 같이 개인정보 보호책임자를 지정하고 있습니다.\n" +
                        "\n" +
                        "▶ 개인정보 보호책임자\n" +
                        "성명 :경은하\n" +
                        "직책 :안드로이드 개발자\n" +
                        "직급 :안드로이드 개발자\n" +
                        "연락처 :ourpageapp@gmail.com,\n" +
                        "※ 개인정보 보호 담당부서로 연결됩니다.\n" +
                        "\n" +
                        "제11조(권익침해 구제방법)\n" +
                        "\n" +
                        "\n" +
                        "\n" +
                        "정보주체는 개인정보침해로 인한 구제를 받기 위하여 개인정보분쟁조정위원회, 한국인터넷진흥원 개인정보침해신고센터 등에 분쟁해결이나 상담 등을 신청할 수 있습니다. 이 밖에 기타 개인정보침해의 신고, 상담에 대하여는 아래의 기관에 문의하시기 바랍니다.\n" +
                        "\n" +
                        "1. 개인정보분쟁조정위원회 : (국번없이) 1833-6972 (www.kopico.go.kr)\n" +
                        "2. 개인정보침해신고센터 : (국번없이) 118 (privacy.kisa.or.kr)\n" +
                        "3. 대검찰청 : (국번없이) 1301 (www.spo.go.kr)\n" +
                        "4. 경찰청 : (국번없이) 182 (ecrm.cyber.go.kr)\n" +
                        "\n" +
                        "「개인정보보호법」제35조(개인정보의 열람), 제36조(개인정보의 정정·삭제), 제37조(개인정보의 처리정지 등)의 규정에 의한 요구에 대 하여 공공기관의 장이 행한 처분 또는 부작위로 인하여 권리 또는 이익의 침해를 받은 자는 행정심판법이 정하는 바에 따라 행정심판을 청구할 수 있습니다.\n" +
                        "\n" +
                        "※ 행정심판에 대해 자세한 사항은 중앙행정심판위원회(www.simpan.go.kr) 홈페이지를 참고하시기 바랍니다.\n" +
                        "\n" +
                        "제12조(개인정보 처리방침 변경)\n" +
                        "\n" +
                        "\n" +
                        "① 이 개인정보처리방침은 2022년 1월 31부터 적용됩니다."
            }
            else -> {
                binding.titleTv.text = getString(R.string.receive_marketing_information)
                binding.contentTv.text = getString(R.string.receive_marketing_information_content)
            }
        }

        //확인 버튼 클릭 리스너 -> 프래그먼트 종료
        binding.confirmBtn.setOnClickListener {
            dismiss()
        }

        return binding.root
    }

    //다이얼로그 위 테두리가 둥글게 돼 있는 테마로 설정
    override fun getTheme(): Int {
        return R.style.BottomSheetDialog
    }
}