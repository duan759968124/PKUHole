package cn.edu.pku.pkuhole.data

import cn.edu.pku.pkuhole.utilities.*

/**
 *
 * @Author:         HuidongQin
 * @e-mail:         hdqin@pku.edu.cn
 * @Time:           2022/1/5
 * @Desc:           存取用户信息，包括token的有效期处理
 * @Version:        1.0
 */
object UserInfoRepository : MMKVOwner {
    // uid
    // name
    // department
    // token
    // token_timestamp

    private var uidFromUserInfo by mmkvString("")
    private var nameFromUserInfo by mmkvString("")
    private var departmentFromUserInfo by mmkvString("")
    private var tokenFromUserInfo by mmkvString("")
    private var tokenTimestampFromUserInfo by mmkvLong(0L)
//    private var userInfo by mmkvParcelable<UserInfo>()
//
//@Parcelize
//data class User(val id: Long, val name: String) : Parcelable
//}

    fun getUid(): String{
        return uidFromUserInfo
    }
    fun setUid(uid : String){
        uidFromUserInfo = uid
    }

    fun getName(): String{
        return nameFromUserInfo
    }
    fun setName(name : String){
        nameFromUserInfo = name
    }

    fun getDepartment(): String{
        return nameFromUserInfo
    }
    fun setDepartment(department : String){
        departmentFromUserInfo = department
    }

    fun getValidToken(): String{
        val durationMilli = System.currentTimeMillis() - tokenTimestampFromUserInfo * 1000
        return if(durationMilli < ONE_HOUR_MILLIS){
            // 上次获取token时间小于一个小时，有效
            tokenFromUserInfo
        }else{
            ""
        }
    }

    fun setToken(token: String){
        tokenFromUserInfo = token
    }

    fun getUserInfo(): UserInfo {
        return UserInfo(uid = uidFromUserInfo,
            name = nameFromUserInfo,
            department = departmentFromUserInfo,
            token = getValidToken(),
            token_timestamp = tokenTimestampFromUserInfo)
    }

    // 目前应该用这个函数作为唯一的更新数据操作
    fun setUserInfo(userInfo: UserInfo){
        uidFromUserInfo = userInfo.uid
        nameFromUserInfo = userInfo.name ?: ""
        departmentFromUserInfo = userInfo.department ?: ""
        tokenFromUserInfo = userInfo.token?: ""
        tokenTimestampFromUserInfo = userInfo.token_timestamp ?: 0L
    }




}