package com.smaart.idrico.model

import android.content.Context
import android.content.res.Resources
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.constraintlayout.widget.ConstraintLayout
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserFactory
import java.io.StringReader

class Parse(private val context: Context, private val resources: Resources) {
    fun layout(xmlString: String, parentLayout: ViewGroup) {
        val parserFactory = XmlPullParserFactory.newInstance()
        val parser: XmlPullParser = parserFactory.newPullParser()
        parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false)
        parser.setInput(StringReader(xmlString))

        var eventType = parser.eventType
        while (eventType != XmlPullParser.END_DOCUMENT) {
            if (eventType == XmlPullParser.START_TAG) {
                val view = createViewFromXml(parser, parentLayout)
                if (view != null) {
                    parentLayout.addView(view)
                }
            }
            eventType = parser.next()
        }
    }

    private fun createViewFromXml(parser: XmlPullParser, parentLayout: ViewGroup): View? {
        val viewName = parser.name
        return when (viewName) {
            "Button" -> createButton(parser, parentLayout)
            else -> null
        }
    }
    private fun createButton(parser: XmlPullParser, parentLayout: ViewGroup): View? {
        val button = Button(context)
        val layoutParams = ConstraintLayout.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        for (i in 0 until parser.attributeCount) {
            val attrName = parser.getAttributeName(i)
            val attrValue = parser.getAttributeValue(i)
            when (attrName) {
                "android:id" -> {
                    val resourceId =
                        resources.getIdentifier(attrValue.substring(1), "id", context.packageName)
                    button.id = resourceId
                }
                "android:text" -> button.text = attrValue
                "android:layout_width" -> layoutParams.width = parseDimension(attrValue)
                "android:layout_height" -> layoutParams.height = parseDimension(attrValue)
                "app:layout_constraintTop_toBottomOf" -> setConstraintTopToBottom(button, attrValue)
                "app:layout_constraintBottom_toBottomOf" -> setConstraintBottom(button, attrValue)
                "app:layout_constraintStart_toStartOf" -> setConstraintStart(button, attrValue)
                "app:layout_constraintEnd_toEndOf" -> setConstraintEnd(button, attrValue)
                "app:layout_constraintTop_toTopOf" -> setConstraintTop(button, attrValue)
            }
        }
        button.layoutParams = layoutParams
        return button
    }

    private fun parseDimension(value: String): Int {
        return when {
            value.endsWith("dp") -> {
                val dp = value.substring(0, value.length - 2).toInt()
                dp * resources.displayMetrics.density.toInt()
            }
            value == "wrap_content" -> ViewGroup.LayoutParams.WRAP_CONTENT
            value == "match_parent" -> ViewGroup.LayoutParams.MATCH_PARENT
            else -> throw IllegalArgumentException("Unsupported dimension format: $value")
        }
    }
    private fun setConstraintTopToBottom(view: View, value: String) {
        if (view.layoutParams is ConstraintLayout.LayoutParams) {
            val targetId = resources.getIdentifier(value.substring(1), "id", context.packageName)
            (view.layoutParams as ConstraintLayout.LayoutParams).topToBottom = targetId
        }
    }
    private fun setConstraintBottom(view: View, value: String) {
        if (view.layoutParams is ConstraintLayout.LayoutParams) {
            (view.layoutParams as ConstraintLayout.LayoutParams).bottomToBottom = ConstraintLayout.LayoutParams.PARENT_ID
        }
    }
    private fun setConstraintEnd(view: View, value: String) {
        if (view.layoutParams is ConstraintLayout.LayoutParams) {
            (view.layoutParams as ConstraintLayout.LayoutParams).endToEnd = ConstraintLayout.LayoutParams.PARENT_ID
        }
    }
    private fun setConstraintStart(view: View, value: String) {
        if (view.layoutParams is ConstraintLayout.LayoutParams) {
            (view.layoutParams as ConstraintLayout.LayoutParams).startToStart = ConstraintLayout.LayoutParams.PARENT_ID
        }
    }
    private fun setConstraintTop(view: View, value: String) {
        if (view.layoutParams is ConstraintLayout.LayoutParams) {
            (view.layoutParams as ConstraintLayout.LayoutParams).topToTop = ConstraintLayout.LayoutParams.PARENT_ID
        }
    }
}