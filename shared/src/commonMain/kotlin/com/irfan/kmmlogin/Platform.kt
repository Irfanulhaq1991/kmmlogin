package com.irfan.kmmlogin

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform