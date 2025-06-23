package com.github.re7r.goautodot.schemes

import com.intellij.util.xmlb.annotations.XCollection

data class SchemeState(
    @XCollection
    var packages: MutableList<String> = mutableListOf()
)