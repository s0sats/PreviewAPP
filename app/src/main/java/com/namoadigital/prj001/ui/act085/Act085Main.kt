package com.namoadigital.prj001.ui.act085

import android.os.Bundle
import android.view.WindowManager
import androidx.fragment.app.Fragment
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import com.namoa_digital.namoa_library.util.HMAux
import com.namoa_digital.namoa_library.util.ToolBox
import com.namoa_digital.namoa_library.view.BaseFragment
import com.namoa_digital.namoa_library.view.Base_Activity_Frag
import com.namoadigital.prj001.databinding.Act085MainBinding
import com.namoadigital.prj001.databinding.Act085MainContentBinding
import com.namoadigital.prj001.model.TUserWorkgroupObj
import com.namoadigital.prj001.model.TWorkgroupObj
import com.namoadigital.prj001.service.WS_Workgroup_Member_Edit
import com.namoadigital.prj001.service.WS_Workgroup_Member_List
import com.namoadigital.prj001.util.Constant
import com.namoadigital.prj001.util.ConstantBaseApp
import com.namoadigital.prj001.util.ToolBox_Con
import com.namoadigital.prj001.util.ToolBox_Inf
import kotlin.collections.ArrayList

class Act085Main :
    Base_Activity_Frag(),
    Act085MainContract.I_View ,
    Act085WorkgroupRemoveListFrg.onWorkgroupRemoveInteract {

    private val WORKGROUP_REMOVE_LIST_FRAG_TAG = "WORKGROUP_REMOVE_LIST_FRAG"
    private lateinit var binding : Act085MainContentBinding
    private val fm = supportFragmentManager
    private lateinit var bundle: Bundle
    private val mPresenter : Act085MainContract.I_Presenter by lazy{
        Act085MainPresenter(
            context,
            this,
            bundle,
            mModule_Code,
            mResource_Code,
        )
    }
    private val workgroupRemoveListFrg: Act085WorkgroupRemoveListFrg by lazy {
        Act085WorkgroupRemoveListFrg.newInstance(
            hmAux_Trans,
            TUserWorkgroupObj(
                userName = "Daniel Luche",
                userCode = 52,
                userImage = null,
                userNick = "luche (52)",
                emailP = "d.luche@namoadigital.com",
                erpCode = null
            ),
            getMockedList()
        )
    }
    //
    private var wsProcess = ""
    //
    private fun getMockedList(): ArrayList<TWorkgroupObj> {
        val mockedJson: String = "[{\n" +
                "            \"customer_code\": \"1\",\n" +
                "            \"group_code\": \"56\",\n" +
                "            \"group_desc\": \"0 Teste Pipeline\",\n" +
                "            \"group_image\": null,\n" +
                "            \"date_expire\": \"2021-09-20 16:20:00\",\n" +
                "            \"active\": \"1\"\n" +
                "        }, {\n" +
                "            \"customer_code\": \"1\",\n" +
                "            \"group_code\": \"60\",\n" +
                "            \"group_desc\": \"actest (10075)\",\n" +
                "            \"group_image\": null,\n" +
                "            \"date_expire\": \"\",\n" +
                "            \"active\": \"0\"\n" +
                "        }, {\n" +
                "            \"customer_code\": \"1\",\n" +
                "            \"group_code\": \"51\",\n" +
                "            \"group_desc\": \"agends\",\n" +
                "            \"group_image\": null,\n" +
                "            \"date_expire\": \"\",\n" +
                "            \"active\": \"0\"\n" +
                "        }, {\n" +
                "            \"customer_code\": \"1\",\n" +
                "            \"group_code\": \"49\",\n" +
                "            \"group_desc\": \"ALANNNNN\",\n" +
                "            \"group_image\": null,\n" +
                "            \"date_expire\": \"\",\n" +
                "            \"active\": \"0\"\n" +
                "        }, {\n" +
                "            \"customer_code\": \"1\",\n" +
                "            \"group_code\": \"50\",\n" +
                "            \"group_desc\": \"ALANNNNN\",\n" +
                "            \"group_image\": null,\n" +
                "            \"date_expire\": \"\",\n" +
                "            \"active\": \"0\"\n" +
                "        }, {\n" +
                "            \"customer_code\": \"1\",\n" +
                "            \"group_code\": \"17\",\n" +
                "            \"group_desc\": \"Aquele dia!\",\n" +
                "            \"group_image\": \"https://s3-eu-west-1.amazonaws.com/idev.namoa.web/1/MD/FILE/175.png\",\n" +
                "            \"date_expire\": \"\",\n" +
                "            \"active\": \"0\"\n" +
                "        }, {\n" +
                "            \"customer_code\": \"1\",\n" +
                "            \"group_code\": \"44\",\n" +
                "            \"group_desc\": \"Bolha\",\n" +
                "            \"group_image\": \"https://s3-eu-west-1.amazonaws.com/idev.namoa.web/1/MD/FILE/300.png\",\n" +
                "            \"date_expire\": \"\",\n" +
                "            \"active\": \"0\"\n" +
                "        }, {\n" +
                "            \"customer_code\": \"1\",\n" +
                "            \"group_code\": \"59\",\n" +
                "            \"group_desc\": \"Btt (10071)\",\n" +
                "            \"group_image\": \"https://s3-eu-west-1.amazonaws.com/idev.namoa.web/1/MD/FILE/337.png\",\n" +
                "            \"date_expire\": \"\",\n" +
                "            \"active\": \"0\"\n" +
                "        }, {\n" +
                "            \"customer_code\": \"1\",\n" +
                "            \"group_code\": \"39\",\n" +
                "            \"group_desc\": \"Btt Auto AP\",\n" +
                "            \"group_image\": null,\n" +
                "            \"date_expire\": \"\",\n" +
                "            \"active\": \"0\"\n" +
                "        }, {\n" +
                "            \"customer_code\": \"1\",\n" +
                "            \"group_code\": \"23\",\n" +
                "            \"group_desc\": \"DlucheXBtt\",\n" +
                "            \"group_image\": \"https://s3-eu-west-1.amazonaws.com/idev.namoa.web/1/MD/FILE/234.png\",\n" +
                "            \"date_expire\": \"\",\n" +
                "            \"active\": \"1\"\n" +
                "        }, {\n" +
                "            \"customer_code\": \"1\",\n" +
                "            \"group_code\": \"37\",\n" +
                "            \"group_desc\": \"dreis\",\n" +
                "            \"group_image\": \"https://s3-eu-west-1.amazonaws.com/idev.namoa.web/1/MD/FILE/212.png\",\n" +
                "            \"date_expire\": \"\",\n" +
                "            \"active\": \"0\"\n" +
                "        }, {\n" +
                "            \"customer_code\": \"1\",\n" +
                "            \"group_code\": \"42\",\n" +
                "            \"group_desc\": \"erro pdf null\",\n" +
                "            \"group_image\": null,\n" +
                "            \"date_expire\": \"\",\n" +
                "            \"active\": \"0\"\n" +
                "        }, {\n" +
                "            \"customer_code\": \"1\",\n" +
                "            \"group_code\": \"24\",\n" +
                "            \"group_desc\": \"FELIPE / STEVEEE\",\n" +
                "            \"group_image\": \"https://s3-eu-west-1.amazonaws.com/idev.namoa.web/1/MD/FILE/173.png\",\n" +
                "            \"date_expire\": \"\",\n" +
                "            \"active\": \"0\"\n" +
                "        }, {\n" +
                "            \"customer_code\": \"1\",\n" +
                "            \"group_code\": \"38\",\n" +
                "            \"group_desc\": \"Frank\",\n" +
                "            \"group_image\": \"https://s3-eu-west-1.amazonaws.com/idev.namoa.web/1/MD/FILE/213.png\",\n" +
                "            \"date_expire\": \"\",\n" +
                "            \"active\": \"0\"\n" +
                "        }, {\n" +
                "            \"customer_code\": \"1\",\n" +
                "            \"group_code\": \"1\",\n" +
                "            \"group_desc\": \"GRUPO 1\",\n" +
                "            \"group_image\": null,\n" +
                "            \"date_expire\": \"\",\n" +
                "            \"active\": \"0\"\n" +
                "        }, {\n" +
                "            \"customer_code\": \"1\",\n" +
                "            \"group_code\": \"2\",\n" +
                "            \"group_desc\": \"GRUPO 2\",\n" +
                "            \"group_image\": \"https://s3-eu-west-1.amazonaws.com/idev.namoa.web/1/MD/FILE/137.png\",\n" +
                "            \"date_expire\": \"\",\n" +
                "            \"active\": \"0\"\n" +
                "        }, {\n" +
                "            \"customer_code\": \"1\",\n" +
                "            \"group_code\": \"3\",\n" +
                "            \"group_desc\": \"GRUPO 3\",\n" +
                "            \"group_image\": null,\n" +
                "            \"date_expire\": \"\",\n" +
                "            \"active\": \"0\"\n" +
                "        }, {\n" +
                "            \"customer_code\": \"1\",\n" +
                "            \"group_code\": \"4\",\n" +
                "            \"group_desc\": \"GRUPO 4 - XXXXXX\",\n" +
                "            \"group_image\": \"https://s3-eu-west-1.amazonaws.com/idev.namoa.web/1/MD/FILE/180.png\",\n" +
                "            \"date_expire\": \"\",\n" +
                "            \"active\": \"0\"\n" +
                "        }, {\n" +
                "            \"customer_code\": \"1\",\n" +
                "            \"group_code\": \"5\",\n" +
                "            \"group_desc\": \"GRUPO 5\",\n" +
                "            \"group_image\": null,\n" +
                "            \"date_expire\": \"\",\n" +
                "            \"active\": \"0\"\n" +
                "        }, {\n" +
                "            \"customer_code\": \"1\",\n" +
                "            \"group_code\": \"31\",\n" +
                "            \"group_desc\": \"GRUPO DA REVOLTA\",\n" +
                "            \"group_image\": \"https://s3-eu-west-1.amazonaws.com/idev.namoa.web/1/MD/FILE/196.png\",\n" +
                "            \"date_expire\": \"\",\n" +
                "            \"active\": \"0\"\n" +
                "        }, {\n" +
                "            \"customer_code\": \"1\",\n" +
                "            \"group_code\": \"63\",\n" +
                "            \"group_desc\": \"Grupo da Zona\",\n" +
                "            \"group_image\": null,\n" +
                "            \"date_expire\": \"\",\n" +
                "            \"active\": \"0\"\n" +
                "        }, {\n" +
                "            \"customer_code\": \"1\",\n" +
                "            \"group_code\": \"65\",\n" +
                "            \"group_desc\": \"Grupo de Outro Site\",\n" +
                "            \"group_image\": null,\n" +
                "            \"date_expire\": \"\",\n" +
                "            \"active\": \"0\"\n" +
                "        }, {\n" +
                "            \"customer_code\": \"1\",\n" +
                "            \"group_code\": \"62\",\n" +
                "            \"group_desc\": \"Grupo do Site\",\n" +
                "            \"group_image\": null,\n" +
                "            \"date_expire\": \"\",\n" +
                "            \"active\": \"0\"\n" +
                "        }, {\n" +
                "            \"customer_code\": \"1\",\n" +
                "            \"group_code\": \"36\",\n" +
                "            \"group_desc\": \"Grupo do tricô\",\n" +
                "            \"group_image\": null,\n" +
                "            \"date_expire\": \"\",\n" +
                "            \"active\": \"0\"\n" +
                "        }, {\n" +
                "            \"customer_code\": \"1\",\n" +
                "            \"group_code\": \"19\",\n" +
                "            \"group_desc\": \"GRUPO POOL\",\n" +
                "            \"group_image\": null,\n" +
                "            \"date_expire\": \"\",\n" +
                "            \"active\": \"0\"\n" +
                "        }, {\n" +
                "            \"customer_code\": \"1\",\n" +
                "            \"group_code\": \"18\",\n" +
                "            \"group_desc\": \"GRUPO TESTE\",\n" +
                "            \"group_image\": \"https://s3-eu-west-1.amazonaws.com/idev.namoa.web/1/MD/FILE/142.png\",\n" +
                "            \"date_expire\": \"\",\n" +
                "            \"active\": \"0\"\n" +
                "        }, {\n" +
                "            \"customer_code\": \"1\",\n" +
                "            \"group_code\": \"10\",\n" +
                "            \"group_desc\": \"Grupo teste Tablet\",\n" +
                "            \"group_image\": null,\n" +
                "            \"date_expire\": \"\",\n" +
                "            \"active\": \"0\"\n" +
                "        }, {\n" +
                "            \"customer_code\": \"1\",\n" +
                "            \"group_code\": \"58\",\n" +
                "            \"group_desc\": \"lblink (10084)\",\n" +
                "            \"group_image\": \"https://s3-eu-west-1.amazonaws.com/idev.namoa.web/1/MD/FILE/336.png\",\n" +
                "            \"date_expire\": \"\",\n" +
                "            \"active\": \"0\"\n" +
                "        }, {\n" +
                "            \"customer_code\": \"1\",\n" +
                "            \"group_code\": \"21\",\n" +
                "            \"group_desc\": \"N-Hugo\",\n" +
                "            \"group_image\": \"https://s3-eu-west-1.amazonaws.com/idev.namoa.web/1/MD/FILE/167.png\",\n" +
                "            \"date_expire\": \"\",\n" +
                "            \"active\": \"0\"\n" +
                "        }, {\n" +
                "            \"customer_code\": \"1\",\n" +
                "            \"group_code\": \"33\",\n" +
                "            \"group_desc\": \"Olha o Sapoh !!!\",\n" +
                "            \"group_image\": \"https://s3-eu-west-1.amazonaws.com/idev.namoa.web/1/MD/FILE/197.png\",\n" +
                "            \"date_expire\": \"\",\n" +
                "            \"active\": \"0\"\n" +
                "        }, {\n" +
                "            \"customer_code\": \"1\",\n" +
                "            \"group_code\": \"34\",\n" +
                "            \"group_desc\": \"Ronaldo\",\n" +
                "            \"group_image\": null,\n" +
                "            \"date_expire\": \"\",\n" +
                "            \"active\": \"0\"\n" +
                "        }, {\n" +
                "            \"customer_code\": \"1\",\n" +
                "            \"group_code\": \"25\",\n" +
                "            \"group_desc\": \"ROOM TESTE\",\n" +
                "            \"group_image\": \"https://s3-eu-west-1.amazonaws.com/idev.namoa.web/1/MD/FILE/190.png\",\n" +
                "            \"date_expire\": \"\",\n" +
                "            \"active\": \"0\"\n" +
                "        }, {\n" +
                "            \"customer_code\": \"1\",\n" +
                "            \"group_code\": \"26\",\n" +
                "            \"group_desc\": \"ROOM TESTE 2\",\n" +
                "            \"group_image\": null,\n" +
                "            \"date_expire\": \"\",\n" +
                "            \"active\": \"0\"\n" +
                "        }, {\n" +
                "            \"customer_code\": \"1\",\n" +
                "            \"group_code\": \"27\",\n" +
                "            \"group_desc\": \"ROOM TESTE 3\",\n" +
                "            \"group_image\": \"https://s3-eu-west-1.amazonaws.com/idev.namoa.web/1/MD/FILE/191.png\",\n" +
                "            \"date_expire\": \"\",\n" +
                "            \"active\": \"0\"\n" +
                "        }, {\n" +
                "            \"customer_code\": \"1\",\n" +
                "            \"group_code\": \"28\",\n" +
                "            \"group_desc\": \"ROOM TESTE 3 - ###\",\n" +
                "            \"group_image\": \"https://s3-eu-west-1.amazonaws.com/idev.namoa.web/1/MD/FILE/194.png\",\n" +
                "            \"date_expire\": \"\",\n" +
                "            \"active\": \"0\"\n" +
                "        }, {\n" +
                "            \"customer_code\": \"1\",\n" +
                "            \"group_code\": \"6\",\n" +
                "            \"group_desc\": \"SALA TESTE\",\n" +
                "            \"group_image\": null,\n" +
                "            \"date_expire\": \"\",\n" +
                "            \"active\": \"0\"\n" +
                "        }, {\n" +
                "            \"customer_code\": \"1\",\n" +
                "            \"group_code\": \"47\",\n" +
                "            \"group_desc\": \"SG\",\n" +
                "            \"group_image\": null,\n" +
                "            \"date_expire\": \"\",\n" +
                "            \"active\": \"0\"\n" +
                "        }, {\n" +
                "            \"customer_code\": \"1\",\n" +
                "            \"group_code\": \"67\",\n" +
                "            \"group_desc\": \"Sprint 4.0.0\",\n" +
                "            \"group_image\": null,\n" +
                "            \"date_expire\": \"\",\n" +
                "            \"active\": \"0\"\n" +
                "        }, {\n" +
                "            \"customer_code\": \"1\",\n" +
                "            \"group_code\": \"9\",\n" +
                "            \"group_desc\": \"STTTTEEEEEVVVVVVEEEEE\",\n" +
                "            \"group_image\": \"https://s3-eu-west-1.amazonaws.com/idev.namoa.web/1/MD/FILE/332.png\",\n" +
                "            \"date_expire\": \"\",\n" +
                "            \"active\": \"0\"\n" +
                "        }, {\n" +
                "            \"customer_code\": \"1\",\n" +
                "            \"group_code\": \"13\",\n" +
                "            \"group_desc\": \"TESTE 2017-12-18 123\",\n" +
                "            \"group_image\": \"https://s3-eu-west-1.amazonaws.com/idev.namoa.web/1/MD/FILE/176.png\",\n" +
                "            \"date_expire\": \"\",\n" +
                "            \"active\": \"0\"\n" +
                "        }, {\n" +
                "            \"customer_code\": \"1\",\n" +
                "            \"group_code\": \"52\",\n" +
                "            \"group_desc\": \"Teste AC\",\n" +
                "            \"group_image\": null,\n" +
                "            \"date_expire\": \"\",\n" +
                "            \"active\": \"0\"\n" +
                "        }, {\n" +
                "            \"customer_code\": \"1\",\n" +
                "            \"group_code\": \"30\",\n" +
                "            \"group_desc\": \"teste Btt\",\n" +
                "            \"group_image\": \"https://s3-eu-west-1.amazonaws.com/idev.namoa.web/1/MD/FILE/334.png\",\n" +
                "            \"date_expire\": \"\",\n" +
                "            \"active\": \"1\"\n" +
                "        }, {\n" +
                "            \"customer_code\": \"1\",\n" +
                "            \"group_code\": \"12\",\n" +
                "            \"group_desc\": \"teste DR\",\n" +
                "            \"group_image\": null,\n" +
                "            \"date_expire\": \"\",\n" +
                "            \"active\": \"0\"\n" +
                "        }, {\n" +
                "            \"customer_code\": \"1\",\n" +
                "            \"group_code\": \"16\",\n" +
                "            \"group_desc\": \"teste DR3\",\n" +
                "            \"group_image\": null,\n" +
                "            \"date_expire\": \"\",\n" +
                "            \"active\": \"0\"\n" +
                "        }, {\n" +
                "            \"customer_code\": \"1\",\n" +
                "            \"group_code\": \"61\",\n" +
                "            \"group_desc\": \"Teste em conjunto Pipeline\",\n" +
                "            \"group_image\": null,\n" +
                "            \"date_expire\": \"\",\n" +
                "            \"active\": \"0\"\n" +
                "        }, {\n" +
                "            \"customer_code\": \"1\",\n" +
                "            \"group_code\": \"7\",\n" +
                "            \"group_desc\": \"TESTE JHONATAN\",\n" +
                "            \"group_image\": null,\n" +
                "            \"date_expire\": \"\",\n" +
                "            \"active\": \"0\"\n" +
                "        }, {\n" +
                "            \"customer_code\": \"1\",\n" +
                "            \"group_code\": \"8\",\n" +
                "            \"group_desc\": \"TESTE JHONATAN / FELIPE\",\n" +
                "            \"group_image\": \"https://s3-eu-west-1.amazonaws.com/idev.namoa.web/1/MD/FILE/179.png\",\n" +
                "            \"date_expire\": \"\",\n" +
                "            \"active\": \"0\"\n" +
                "        }, {\n" +
                "            \"customer_code\": \"1\",\n" +
                "            \"group_code\": \"66\",\n" +
                "            \"group_desc\": \"Teste Notificação\",\n" +
                "            \"group_image\": null,\n" +
                "            \"date_expire\": \"\",\n" +
                "            \"active\": \"0\"\n" +
                "        }, {\n" +
                "            \"customer_code\": \"1\",\n" +
                "            \"group_code\": \"20\",\n" +
                "            \"group_desc\": \"Teste Novo Agendamento\",\n" +
                "            \"group_image\": \"https://s3-eu-west-1.amazonaws.com/idev.namoa.web/1/MD/FILE/164.png\",\n" +
                "            \"date_expire\": \"\",\n" +
                "            \"active\": \"0\"\n" +
                "        }, {\n" +
                "            \"customer_code\": \"1\",\n" +
                "            \"group_code\": \"22\",\n" +
                "            \"group_desc\": \"Teste Ste\",\n" +
                "            \"group_image\": \"https://s3-eu-west-1.amazonaws.com/idev.namoa.web/1/MD/FILE/171.png\",\n" +
                "            \"date_expire\": \"\",\n" +
                "            \"active\": \"0\"\n" +
                "        }, {\n" +
                "            \"customer_code\": \"1\",\n" +
                "            \"group_code\": \"14\",\n" +
                "            \"group_desc\": \"Testing 18-12\",\n" +
                "            \"group_image\": \"https://s3-eu-west-1.amazonaws.com/idev.namoa.web/1/MD/FILE/189.png\",\n" +
                "            \"date_expire\": \"\",\n" +
                "            \"active\": \"0\"\n" +
                "        }, {\n" +
                "            \"customer_code\": \"1\",\n" +
                "            \"group_code\": \"57\",\n" +
                "            \"group_desc\": \"thiago_testes (10106)\",\n" +
                "            \"group_image\": null,\n" +
                "            \"date_expire\": \"\",\n" +
                "            \"active\": \"0\"\n" +
                "        }, {\n" +
                "            \"customer_code\": \"1\",\n" +
                "            \"group_code\": \"45\",\n" +
                "            \"group_desc\": \"TICKET\",\n" +
                "            \"group_image\": null,\n" +
                "            \"date_expire\": \"\",\n" +
                "            \"active\": \"0\"\n" +
                "        }, {\n" +
                "            \"customer_code\": \"1\",\n" +
                "            \"group_code\": \"54\",\n" +
                "            \"group_desc\": \"TICKET - PIPELINE\",\n" +
                "            \"group_image\": null,\n" +
                "            \"date_expire\": \"\",\n" +
                "            \"active\": \"0\"\n" +
                "        }, {\n" +
                "            \"customer_code\": \"1\",\n" +
                "            \"group_code\": \"46\",\n" +
                "            \"group_desc\": \"Ticket-teste\",\n" +
                "            \"group_image\": null,\n" +
                "            \"date_expire\": \"\",\n" +
                "            \"active\": \"0\"\n" +
                "        }, {\n" +
                "            \"customer_code\": \"1\",\n" +
                "            \"group_code\": \"68\",\n" +
                "            \"group_desc\": \"Tickets antigos\",\n" +
                "            \"group_image\": null,\n" +
                "            \"date_expire\": \"\",\n" +
                "            \"active\": \"0\"\n" +
                "        }, {\n" +
                "            \"customer_code\": \"1\",\n" +
                "            \"group_code\": \"35\",\n" +
                "            \"group_desc\": \"Tst\",\n" +
                "            \"group_image\": null,\n" +
                "            \"date_expire\": \"\",\n" +
                "            \"active\": \"0\"\n" +
                "        }, {\n" +
                "            \"customer_code\": \"1\",\n" +
                "            \"group_code\": \"32\",\n" +
                "            \"group_desc\": \"Tst Edição - Filter\",\n" +
                "            \"group_image\": null,\n" +
                "            \"date_expire\": \"\",\n" +
                "            \"active\": \"0\"\n" +
                "        }, {\n" +
                "            \"customer_code\": \"1\",\n" +
                "            \"group_code\": \"48\",\n" +
                "            \"group_desc\": \"Tst_T\",\n" +
                "            \"group_image\": null,\n" +
                "            \"date_expire\": \"\",\n" +
                "            \"active\": \"0\"\n" +
                "        }, {\n" +
                "            \"customer_code\": \"1\",\n" +
                "            \"group_code\": \"29\",\n" +
                "            \"group_desc\": \"Verificação de ícone padrão\",\n" +
                "            \"group_image\": null,\n" +
                "            \"date_expire\": \"\",\n" +
                "            \"active\": \"0\"\n" +
                "        }\n" +
                "    ]"
        val gson = GsonBuilder().serializeNulls().create()

        val fromJson = gson.fromJson<ArrayList<TWorkgroupObj>>(
            mockedJson,
            object : TypeToken<ArrayList<TWorkgroupObj?>?>() {}.type
        )
//        return fromJson.filter {
//            it.active == 1
//        } as ArrayList<TWorkgroupObj>

        return arrayListOf()

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val mainBinding = Act085MainBinding.inflate(layoutInflater)
        setContentView(mainBinding.root)
        setSupportActionBar(mainBinding.toolbar)
        //
        binding = mainBinding.act085MainContent
        initBundle(savedInstanceState)
        iniSetup()
        iniTrans()
        initVars()
        iniUIFooter()
    }

    private fun initBundle(savedInstanceState: Bundle?) {
        bundle = (savedInstanceState?: intent.extras?: Bundle()) as Bundle
    }

    private fun iniSetup() {
        mResource_Code = ToolBox_Inf.getResourceCode(
            context,
            mModule_Code,
            ConstantBaseApp.ACT085
        )
        //10/06/2021 - Add recolhimento do teclado
        window.setSoftInputMode(

            WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN
                or WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)
    }

    private fun iniTrans() {
        hmAux_Trans = mPresenter.getTranslation()
    }

    private fun initVars() {
        setFrag(workgroupRemoveListFrg,WORKGROUP_REMOVE_LIST_FRAG_TAG)
        //
        mPresenter.executeWorkgroupMemberListService(52)
    }

    private fun <T : BaseFragment?> setFrag(type: T, sTag: String) {
        if (fm.findFragmentByTag(sTag) == null) {
            val ft = fm.beginTransaction()
            ft.replace(binding.act085FrgPlaceholder.id, type as Fragment, sTag)
            ft.addToBackStack(null)
            ft.commit()
        }
    }

    override fun setWsProcess(wsProcess: String) {
        this.wsProcess = wsProcess
    }

    override fun showPD(ttl: String, msg: String) {
        enableProgressDialog(
            ttl,
            msg,
            hmAux_Trans["sys_alert_btn_cancel"],
            hmAux_Trans["sys_alert_btn_ok"]
        )
    }

    override fun showAlert(ttl: String, msg: String) {
        ToolBox.alertMSG(
            context,
            ttl,
            msg,
            null,
            0
        )
    }


    //region Act085WorkgroupRemoveListFrg.onWorkgroupRemoveInteract
    override fun callWorkgroupEditService(userCode: Int, action: Int, workgroupCode: Int) {
        mPresenter.executeWorkgroupEditService(userCode,action, arrayListOf(workgroupCode))
    }

    override fun updateLinkeWorkgroupListIntoFrag(wgMemberList: List<TWorkgroupObj>) {
        workgroupRemoveListFrg?.let{
            it.updateLinkedWorkgroupList(wgMemberList as ArrayList<TWorkgroupObj>)
        }
    }
    //endregion

    private fun iniUIFooter() {
        iniFooter()
        //
        mUser_Info = ToolBox_Con.getPreference_User_Code_Nick(context)
        mAct_Info = Constant.ACT085
        mAct_Title = Constant.ACT085 + "_" + "title"
        //
        val mFooter = ToolBox_Inf.loadFooterSiteOperationInfo(context)
        mSite_Value = mFooter[Constant.FOOTER_SITE]
        mOperation_Value = mFooter[Constant.FOOTER_OPERATION]
        //
        setUILanguage(hmAux_Trans)
        setMenuLanguage(hmAux_Trans)
        setTitleLanguage()
        setFooter()
    }

    override fun footerCreateDialog() {
        ToolBox_Inf.buildFooterDialog(context)
    }

    override fun processCloseACT(mLink: String?, mRequired: String?) {
        super.processCloseACT(mLink, mRequired)
        processCloseACT(mLink,mRequired, HMAux())
    }

    override fun processCloseACT(mLink: String?, mRequired: String?, hmAux: HMAux?) {
        super.processCloseACT(mLink, mRequired, hmAux)
        //
        when(wsProcess){
            WS_Workgroup_Member_List::class.java.name ->{
                wsProcess = ""
                progressDialog.dismiss()
                //
                mPresenter.processWgMemberListReturn(mLink)
            }
            WS_Workgroup_Member_Edit::class.java.name ->{
                progressDialog.dismiss()
                mPresenter.executeWorkgroupMemberListService(52)
            }
            else -> {
                progressDialog.dismiss()
            }

        }
    }
}