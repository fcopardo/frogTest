package com.pardo.frogmitest.domain.models.domain

import com.pardo.frogmitest.domain.models.ui.StoreCellData

data class StoresRequestResult(val stores : List<StoreCellData>, val origin: String)