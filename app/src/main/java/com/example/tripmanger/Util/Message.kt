package com.example.tripmanger.Util

import android.content.Context
import android.widget.Toast


class Message
{
    companion object
    {
        fun Message(context : Context, str : String)
        {
            Toast.makeText(context,str,Toast.LENGTH_SHORT).show()
        }
    }
}