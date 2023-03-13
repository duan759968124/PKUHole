package cn.edu.pku.treehole.data

import android.annotation.SuppressLint
import android.text.TextUtils
import cn.edu.pku.treehole.BuildConfig
import cn.edu.pku.treehole.utilities.*
import java.text.SimpleDateFormat
import java.util.*

/**
 *
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
//    private var localName by mmkvString("")
//    private var localDepartment by mmkvString("")
//    private var localToken by mmkvString("")
    var localUserInfo by mmkvParcelable<UserInfo>()
    var localJwtTimestamp by mmkvLong(0L)

    var localGlobalHoleContentCurrentTextSize by mmkvInt(14)
    var localHoleContentDefaultTextSize by mmkvInt(14)
    var localHoleContentMaxTextSize by mmkvInt(22)
    var localHoleContentMinTextSize by mmkvInt(13)
//    private var userInfo by mmkvParcelable<UserInfo>()
//    @Parcelize
//    data class User(val id: Long, val name: String) : Parcelable
//    }
    // 0-跟随系统 1-日间模式 2-夜间模式
    var localDarkMode by mmkvInt(0)
    var localUIDarkMode by mmkvBool(true)
    var localSetQuote by mmkvBool(true)

    var lastReplyCommentId by mmkvLong(0L)
    var lastReplyContent by mmkvString("")
    var lastReplyTimeStamp by mmkvLong(0L)

    fun getUid(): String{
        return localUid
    }
    fun setUid(uid : String){
        localUid = uid
    }


    fun getValidToken(): String{
        val durationMilli = System.currentTimeMillis() - localJwtTimestamp * 1000
//        return ""
        return if(durationMilli < 24 * ONE_HOUR_MILLIS){
            // 上次获取token时间小于24个小时，有效
            localUserInfo!!.jwt
        }else{
            ""
        }
    }

//    fun setToken(token: String){
//        tokenFromUserInfo = token
//    }

//    fun getUserInfo(): UserInfo {
//        return UserInfo(uid = localUid,
//            name = localName,
//            department = localDepartment,
//            token = getValidToken(),
//            )
//    }

    // 目前应该用这个函数作为唯一的更新数据操作
//    fun setUserInfo(userInfo: UserInfo) {
//        localUid = userInfo.uid
//        localName = userInfo.name ?: ""
//        localDepartment = userInfo.department ?: ""
//        localToken = userInfo.token ?: ""
//        localTokenTimestamp = userInfo.token_timestamp ?: 0L
//    }

    var localUpdateInfo by mmkvParcelable<UpdateInfo>()

    // 登录信息数据
    // account
    // password
    // passwordSecure
    private var localAccount by mmkvString("")
    private var localPasswordSecure by mmkvString("")

    fun getAccount(): String {
        return localAccount
    }

    fun setAccount(account: String) {
        localAccount = account
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

    private fun setAppInstallId(appInstallId: String){
        localAppInstallId = appInstallId
    }

    // firstStartApp  是否第一次安装启动
    private var firstStartApp by mmkvBool(true)

    fun setISFirstStart(isFirstStart: Boolean){
        firstStartApp = isFirstStart
    }

    fun isFirstStart(): Boolean{
        if(firstStartApp){
            // 改变是否是第一次启动状态【不改变了，手动改变】
//            firstStartApp = false
            return true
        }
        return false
    }

    // 当前版本是不是第一次启动
    private var localLastAppVersionCode by mmkvInt(0)

    private fun getLastAppVersionCode(): Int{
        return localLastAppVersionCode
    }

    private fun setLastAppVersionCode(versioncode: Int){
        localLastAppVersionCode = versioncode
    }

    fun isFirstStartCurVersion(): Boolean{
        val curVersionCode: Int = BuildConfig.VERSION_CODE
        if(curVersionCode > getLastAppVersionCode()){
            // 当前版本大于上次版本code, 该版本属于第一次启动
            setLastAppVersionCode(curVersionCode)
            return true
        }
        return false
    }

    // 是不是今天第一次启动
    private var localLastAppStartDate by mmkvString("2022-04-06")

    private fun getLastAppStartDate(): String{
        return localLastAppStartDate
    }

    private fun setLastAppStartDate(lastAppStartDate: String){
        localLastAppStartDate = lastAppStartDate
    }

     @SuppressLint("SimpleDateFormat")
     fun isFirstStartToday(): Boolean{
         val today: String = SimpleDateFormat("yyyy-MM-dd").format(Date())
         if (!TextUtils.isEmpty(today) && !TextUtils.isEmpty(getLastAppStartDate())) {
             if (localLastAppStartDate != today) {
                 setLastAppStartDate(today)
                 return true
             }
         }
         return false
     }

    var localRandomTip by mmkvString("")
    var hiddenRandomTipToday by mmkvBool(false)
    var localShowTipDay by mmkvString("2022-04-06")

    fun checkIsShowTip(): Boolean {
        // 今天不显示的条件:已经显示过并且点击了复选框
        val today: String = SimpleDateFormat("yyyy-MM-dd").format(Date())
        if (!TextUtils.isEmpty(today) && !TextUtils.isEmpty(localShowTipDay)) {
            return if (localShowTipDay != today) {
                localShowTipDay = today
                hiddenRandomTipToday = false
                true
            } else {
                !hiddenRandomTipToday
            }
        }
        return true
    }


    fun clearAll() {
        // 清理全部数据前需要保留的数据: 用户账号、uuid
        val account = getAccount()
        val appInstallId = getAppInstallId()
        val saveVersionCode = getLastAppVersionCode()
//        val saveStartAppDate = getLastAppStartDate()
        kv.clearAll()
        firstStartApp = false
        setLastAppVersionCode(versioncode = saveVersionCode)
//        setLastAppStartDate(lastAppStartDate=saveStartAppDate)
        setAccount(account = account)
        setAppInstallId(appInstallId = appInstallId)
    }

}