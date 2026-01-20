package com.sunstrinq.ffmpegkitlab.di

import com.sunstrinq.ffmpegkitlab.ui.screen.HomeViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val homeModule = module {
    viewModelOf(::HomeViewModel)
}