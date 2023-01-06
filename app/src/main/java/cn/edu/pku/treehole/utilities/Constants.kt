package cn.edu.pku.treehole.utilities

/**
 *
 * @Author:         HuidongQin
 * @e-mail:         hdqin@pku.edu.cn
 * @Time:           2021/12/24
 * @Desc:
 * @Version:        1.0
 */

const val DATABASE_NAME = "pku_hole_db"
const val PRE_POPULATE_HOLE_LIST_DATA = "hole_all_list.js"

// API
//const val HOLE_HOST_ADDRESS = "https://pkuhelper.pku.edu.cn/"
const val HOLE_HOST_ADDRESS = "https://treehole.pku.edu.cn/"

const val HOLE_UPDATE_HOST_ADDRESS = "https://its.pku.edu.cn/"

const val ISOP_HOST_ADDRESS = "https://isop.pku.edu.cn/"

// 连接超时时间
const val HTTP_TIMEOUT_CONNECT: Long = 15

// 读取数据超时时间
const val HTTP_TIMEOUT_READ: Long = 15

// webview
const val USER_AGREEMENT_URL =
//    "https://its.pku.edu.cn/pku_gateway_apps/docs/PKU_Hole_User_Agreement.html"
    "https://treehole.pku.edu.cn/PKU_Hole_User_Agreement.html"
const val PRIVACY_POLICY_URL =
    "https://its.pku.edu.cn/pku_gateway_apps/docs/PKU_Hole_Privacy_Policy.html"

// App Info
const val APP_NAME: String = "PKUHOLE"