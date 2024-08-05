package com.worksy.hr.art.models.travelRequest

data class InProgressResponse(var data: List<Data>){
    data class Data(
        var trID:String,var trType:String,var titletxt:String,var subTitle:String,var type:String,var destination:String,var date:String,var budget:String,var status:String,var stage:String
    )
}