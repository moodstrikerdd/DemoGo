package com.moo.demogo.utils

import java.math.BigDecimal


/**
 * @ClassName:      GraphUtils
 * @Author:         moodstrikerdd
 * @CreateDate:     2019/12/19 16:17
 * @Description:    java类作用描述
 */
class GraphUtils {
    companion object {
        fun getMoneyString(float: Float) = when {
            float >= 100000000f -> "${BigDecimal((float / 100000000f).toString()).setScale(2, BigDecimal.ROUND_HALF_UP)}亿"

            float >= 10000f -> "${BigDecimal((float / 10000f).toString()).setScale(2, BigDecimal.ROUND_HALF_UP)}万"
            else -> "${BigDecimal(float.toString()).setScale(2, BigDecimal.ROUND_HALF_UP)}元"
        }

        fun getPercentageString(float: Float) = "${BigDecimal(float.toString()).setScale(2, BigDecimal.ROUND_HALF_UP)}%"

    }
}