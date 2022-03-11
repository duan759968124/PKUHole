package cn.edu.pku.pkuhole.data

import cn.edu.pku.pkuhole.utilities.*
import java.util.*

/**
 *
 * @Author:         HuidongQin
 * @e-mail:         hdqin@pku.edu.cn
 * @Time:           2022/1/5
 * @Desc:           存取用户信息，包括token的有效期处理
 * @Version:        1.0
 */
object LocalRepository : MMKVOwner {
    // 用户信息数据
    // uid
    // name
    // department
    // token
    // token_timestamp
    private var localUid by mmkvString("")
    private var localName by mmkvString("")
    private var localDepartment by mmkvString("")
    private var localToken by mmkvString("")
    private var localTokenTimestamp by mmkvLong(0L)
//    private var userInfo by mmkvParcelable<UserInfo>()
//    @Parcelize
//    data class User(val id: Long, val name: String) : Parcelable
//    }

    fun getUid(): String{
        return localUid
    }
//    fun setUid(uid : String){
//        uidFromUserInfo = uid
//    }

//    fun getName(): String{
//        return nameFromUserInfo
//    }
//    fun setName(name : String){
//        nameFromUserInfo = name
//    }

//    fun getDepartment(): String{
//        return nameFromUserInfo
//    }
//    fun setDepartment(department : String){
//        departmentFromUserInfo = department
//    }

    fun getValidToken(): String{
        val durationMilli = System.currentTimeMillis() - localTokenTimestamp * 1000
//        return ""
        return if(durationMilli < ONE_HOUR_MILLIS){
            // 上次获取token时间小于一个小时，有效
            localToken
        }else{
            ""
        }
    }

//    fun setToken(token: String){
//        tokenFromUserInfo = token
//    }

    fun getUserInfo(): UserInfo {
        return UserInfo(uid = localUid,
            name = localName,
            department = localDepartment,
            token = getValidToken(),
            token_timestamp = localTokenTimestamp)
    }

    // 目前应该用这个函数作为唯一的更新数据操作
    fun setUserInfo(userInfo: UserInfo){
        localUid = userInfo.uid
        localName = userInfo.name ?: ""
        localDepartment = userInfo.department ?: ""
        localToken = userInfo.token?: ""
        localTokenTimestamp = userInfo.token_timestamp ?: 0L
    }

    // 登录信息数据
    // account
    // password
    // passwordSecure
    private var localAccount by mmkvString("")
    private var localPassword by mmkvString("")
    private var localPasswordSecure by mmkvString("")

    fun getAccount(): String{
        return localAccount
    }
    fun setAccount(account: String){
        localAccount = account
    }
    fun getPassword(): String{
        return localPassword
    }
    fun setPassword(password: String){
        localPassword = password
    }

    fun getPasswordSecure(): String{
        return localPasswordSecure
    }

    fun setPasswordSecure(pwdSecure: String){
        localPasswordSecure = pwdSecure
    }

    // install uuid 设备唯一设备码
    private var localAppInstallId by mmkvString("")
    fun getAppInstallId(): String{
        if(localAppInstallId.isEmpty()){
            setAppInstallId(UUID.randomUUID().toString())
        }
        return localAppInstallId
    }

    fun setAppInstallId(appInstallId: String){
        localAppInstallId = appInstallId
    }

    fun clearAll(){
        // 清理全部数据前需要保留的数据: 用户账号、uuid
        val account = getAccount()
        val appInstallId = getAppInstallId()
        kv.clearAll()
        setAccount(account = account)
        setAppInstallId(appInstallId = appInstallId)
    }




}