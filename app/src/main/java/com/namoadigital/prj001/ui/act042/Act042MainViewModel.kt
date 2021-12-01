package com.namoadigital.prj001.ui.act042

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.namoadigital.prj001.dao.SO_Pack_Express_LocalDao
import com.namoadigital.prj001.model.SO_Pack_Express_Local
import com.namoadigital.prj001.sql.SO_Pack_Express_Local_Sql_009
import com.namoadigital.prj001.util.Constant
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*

class Act042MainViewModel(val customerCode: Long, val expressLocalDao: SO_Pack_Express_LocalDao): ViewModel() {

    val so_express_list: MutableLiveData<ArrayList<SO_Pack_Express_Local>> = MutableLiveData<ArrayList<SO_Pack_Express_Local>>().apply {
        (mutableListOf<SO_Pack_Express_Local>())
    }

    suspend fun getSoExpressList(){
        withContext(Dispatchers.Default){

        so_express_list.postValue(
            expressLocalDao.query(
                SO_Pack_Express_Local_Sql_009(
                    customerCode,
                    Constant.RETURN_SQL_HM_AUX
                ).toSqlQuery()
            ) as ArrayList<SO_Pack_Express_Local>?)
        }
    }

    fun getData(){
        viewModelScope.launch{
            getSoExpressList()
        }
    }


}

class Act042MainViewModelFactory(private val customerCode: Long, private val expressLocalDao: SO_Pack_Express_LocalDao) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(Act042MainViewModel::class.java)) {
            return Act042MainViewModel(customerCode, expressLocalDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

