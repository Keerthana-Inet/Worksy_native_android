package com.worksy.hr.art.models.claimRequestResponse
data class AddClaimSettingsResponse(
    val `data`: Data,
    val error: Any,
    val message: String,
    val status: String
)
{
    data class Data(
        val groups: List<Group>
    )

    {
        data class Group(
            val groupId: String,
            val groupName: String,
            val itemListing: List<ItemListing>,
            val items: List<Item>
        )
        {
            data class ItemListing(
                val id: String,
                val name: String
            )

            data class Item(
                val fields: Fields,
                val itemId: String,
                val itemName: String
            )
            {
                data class Fields(
                    val attachment: String,
                    val customFields: List<CustomField>,
                    val reason: String,
                    val tax: String,
                    val remarks: String
                )
                {
                    data class CustomField(
                        val isCompulsory: String,
                        val key: String,
                        val options: List<Option>,
                        val title: String,
                        val type: String
                    )
                    {
                        data class Option(
                            val key: String,
                            val value: String
                        )
                    }

                }

            }

        }

    }

}
