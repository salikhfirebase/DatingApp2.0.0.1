package com.dreams.best.Model

import com.google.firebase.database.IgnoreExtraProperties
import com.google.firebase.database.PropertyName


/**
 * Created by Andriy Deputat email(andriy.deputat@gmail.com) on 4/1/19.
 */
@IgnoreExtraProperties
data class Conversion(
    var conversionEvent: String? = null,
    @get: PropertyName("offer_id") @set:PropertyName("offer_id") var offerId: String? = null,
    @get: PropertyName("l") @set:PropertyName("l") var l: String? = null
)