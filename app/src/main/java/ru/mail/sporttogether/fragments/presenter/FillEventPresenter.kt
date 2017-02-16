package ru.mail.sporttogether.fragments.presenter

import ru.mail.sporttogether.mvp.MapPresenter

/**
 * Created by bagrusss on 14.02.17
 */
abstract class FillEventPresenter : MapPresenter() {

    abstract fun  fillEvent(name: String,
                            sport: String,
                            maxPeople: Int,
                            description: String,
                            addressText:String,
                            time: Long,
                            joinToEvent: Boolean,
                            needAddTemplate: Boolean)

    abstract fun loadCategoriesBySubname(subname: String)

}