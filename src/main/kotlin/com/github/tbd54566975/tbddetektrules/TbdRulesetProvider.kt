package com.github.tbd54566975.tbddetektrules

import io.gitlab.arturbosch.detekt.api.Config
import io.gitlab.arturbosch.detekt.api.RuleSet
import io.gitlab.arturbosch.detekt.api.RuleSetProvider

class TbdRulesetProvider : RuleSetProvider {
    override val ruleSetId: String = "TbdRulesetProvider"

    override fun instance(config: Config): RuleSet {
        return RuleSet(
            ruleSetId,
            listOf(
                JvmOverloadsAnnotationRule(config),
            ),
        )
    }
}
