package com.jakionmobile.storyapp

import com.jakionmobile.storyapp.data.api.ListStoryItem

object DataDummy {
    fun generateDummyStoryResponse(): List<ListStoryItem> {
        val items: MutableList<ListStoryItem> = arrayListOf()
        for (i in 0..100) {
            val quote = ListStoryItem(
                "photoUrl $i",
                "createdAt $i",
                "name $i",
                "description $i",
                0.0,
                "$1",
                0.0,
            )
            items.add(quote)
        }
        return items
    }
}