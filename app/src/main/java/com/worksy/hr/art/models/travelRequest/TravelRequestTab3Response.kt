package com.worksy.hr.art.models.travelRequest

data class TravelRequestTab3Response(var data: List<RequestData>){
    data class RequestData(
        var image:Int,var username:String,var designation:String
    )
}