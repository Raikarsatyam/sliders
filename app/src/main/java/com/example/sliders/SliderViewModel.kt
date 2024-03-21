package com.example.sliders

import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.filterNotNull

class SliderViewModel : ViewModel() {
    private val _data = MutableStateFlow<Data?>(null)
    val data = _data.asStateFlow().filterNotNull()

    private val _firstCir = MutableStateFlow(0)
    val firstCir = _firstCir.asStateFlow()

    private val _secCir = MutableStateFlow(0)
    val secCir = _secCir.asStateFlow()

    private var currentTotal = 0

    private val _progress = MutableStateFlow(0f)
    val progress = _progress.asStateFlow()

    var isDialogVisible = MutableStateFlow(false)
        private set

    var currentProgressPosition = MutableStateFlow(0)
        private set

    var saveMultiplier = 0

    var saveQtyValue = 0

    fun showDialog(isVisible: Boolean) {
        isDialogVisible.value = isVisible
    }

    init {
        setData()
        calculateDistance()
    }

    private fun setData() {
        val json = """
        {
            "min": 0,
            "max": 1000,
            "points": [
                {"value": 200},
                {"value": 700}
            ],
            "items": [
                {
                    "name": "item A",
                    "multiple": 3,
                    "eachQtyValue": 75
                },
                {
                    "name": "item B",
                    "multiple": 1,
                    "eachQtyValue": 50
                },
                {
                    "name": "item C",
                    "multiple": 2,
                    "eachQtyValue": 20
                }
            ]
        }
    """.trimIndent()
        _data.value = Gson().fromJson(json, Data::class.java)
    }

    private fun calculateDistance() {
        _data.value?.apply {
            _firstCir.value = points[0].value.times(253).div(max)
            _secCir.value = points[1].value.times(253).div(max)
        }
    }

    fun totalCost(isPlus: Boolean, quantity: Quantity) {
        _data.value?.apply {
            val selectedItem = when (quantity) {
                Quantity.ONE -> items[1]
                Quantity.TWO -> items[2]
                Quantity.THREE -> items[0]
            }
            val value = selectedItem.eachQtyValue.times(selectedItem.multiple)
            if ((isPlus && currentTotal <= max) || (!isPlus && currentTotal >= 0)) {
                currentTotal = if (isPlus) currentTotal + value else currentTotal - value
                val dis = currentTotal.times(253f).div(max)
                _progress.value = dis.div(253f)
            }
            currentProgressPosition.value = currentTotal.times(253).div(max)
        }
    }

    fun updateProgress(it: Int) {
        _data.value?.apply {
            currentTotal = it
            val dis = currentTotal.times(253f).div(max)
            _progress.value = if (currentTotal <= max) dis.div(253f) else 1f
        }
    }
}

enum class Quantity { ONE, TWO, THREE }