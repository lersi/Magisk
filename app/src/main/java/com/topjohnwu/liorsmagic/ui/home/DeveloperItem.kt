package com.topjohnwu.liorsmagic.ui.home

import com.topjohnwu.liorsmagic.R
import com.topjohnwu.liorsmagic.core.Const
import com.topjohnwu.liorsmagic.databinding.RvItem

interface Dev {
    val name: String
}

private interface JohnImpl : Dev {
    override val name get() = "topjohnwu"
}

private interface VvbImpl : Dev {
    override val name get() = "vvb2060"
}

private interface YUImpl : Dev {
    override val name get() = "yujincheng08"
}

private interface RikkaImpl : Dev {
    override val name get() = "RikkaW"
}

sealed class DeveloperItem : Dev {

    abstract val items: List<IconLink>
    val handle get() = "@${name}"

    object John : DeveloperItem(), JohnImpl {
        override val items =
            listOf(
                object : IconLink.Twitter(), JohnImpl {},
                IconLink.Github.Project
            )
    }

    object Vvb : DeveloperItem(), VvbImpl {
        override val items =
            listOf<IconLink>(
                object : IconLink.Twitter(), VvbImpl {},
                object : IconLink.Github.User(), VvbImpl {}
            )
    }

    object YU : DeveloperItem(), YUImpl {
        override val items =
            listOf<IconLink>(
                object : IconLink.Twitter() { override val name = "shanasaimoe" },
                object : IconLink.Github.User(), YUImpl {},
                object : IconLink.Sponsor(), YUImpl {}
            )
    }

    object Rikka : DeveloperItem(), RikkaImpl {
        override val items =
            listOf<IconLink>(
                object : IconLink.Twitter() { override val name = "rikkawww" },
                object : IconLink.Github.User(), RikkaImpl {}
            )
    }
}

sealed class IconLink : RvItem() {

    abstract val icon: Int
    abstract val title: Int
    abstract val link: String

    override val layoutRes get() = R.layout.item_icon_link

    abstract class PayPal : IconLink(), Dev {
        override val icon get() = R.drawable.ic_paypal
        override val title get() = R.string.paypal
        override val link get() = "https://paypal.me/$name"

        object Project : PayPal() {
            override val name: String get() = "liorsmagicdonate"
        }
    }

    object Patreon : IconLink() {
        override val icon get() = R.drawable.ic_patreon
        override val title get() = R.string.patreon
        override val link get() = Const.Url.PATREON_URL
    }

    abstract class Twitter : IconLink(), Dev {
        override val icon get() = R.drawable.ic_twitter
        override val title get() = R.string.twitter
        override val link get() = "https://twitter.com/$name"
    }

    abstract class Github : IconLink() {
        override val icon get() = R.drawable.ic_github
        override val title get() = R.string.github

        abstract class User : Github(), Dev {
            override val link get() = "https://github.com/$name"
        }

        object Project : Github() {
            override val link get() = Const.Url.SOURCE_CODE_URL
        }
    }

    abstract class Sponsor : IconLink(), Dev {
        override val icon get() = R.drawable.ic_favorite
        override val title get() = R.string.github
        override val link get() = "https://github.com/sponsors/$name"
    }
}
