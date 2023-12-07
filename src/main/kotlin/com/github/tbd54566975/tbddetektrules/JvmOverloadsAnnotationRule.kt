package com.github.tbd54566975.tbddetektrules

import io.gitlab.arturbosch.detekt.api.CodeSmell
import io.gitlab.arturbosch.detekt.api.Config
import io.gitlab.arturbosch.detekt.api.Debt
import io.gitlab.arturbosch.detekt.api.Entity
import io.gitlab.arturbosch.detekt.api.Issue
import io.gitlab.arturbosch.detekt.api.Rule
import io.gitlab.arturbosch.detekt.api.Severity
import io.gitlab.arturbosch.detekt.rules.isAbstract
import org.jetbrains.kotlin.psi.KtClass
import org.jetbrains.kotlin.psi.KtNamedFunction
import org.jetbrains.kotlin.psi.KtParameter
import org.jetbrains.kotlin.psi.KtUserType
import org.jetbrains.kotlin.psi.psiUtil.getParentOfType

class JvmOverloadsAnnotationRule(config: Config) : Rule(config) {
  override val issue = Issue(
    javaClass.simpleName,
    Severity.Warning,
    "This rule reports functions that have default parameters but are not annotated with @JvmOverloads.",
    Debt.FIVE_MINS,
  )

  override fun visitNamedFunction(function: KtNamedFunction) {
    super.visitNamedFunction(function)

    val isValidContext = function.isMemberOfAClassOrObject() || function.isTopLevel()
    val isAbstractMethod = function.isAbstract()

    if (
      isValidContext &&
      !isAbstractMethod &&
      function.hasDefaultParameters() &&
      !function.hasJvmOverloadsAnnotation()
    ) {
      report(
        CodeSmell(
          issue,
          Entity.from(function),
          message = "The function `${function.name}` has default parameters and should be annotated with @JvmOverloads."
        )
      )
    }
  }

  private fun KtNamedFunction.hasDefaultParameters(): Boolean =
    this.valueParameters.any(KtParameter::hasDefaultValue)

  private fun KtNamedFunction.hasJvmOverloadsAnnotation(): Boolean {
    return this.annotationEntries.any { annotationEntry ->
      val typeReference = annotationEntry.typeReference
      val typeElement = typeReference?.typeElement
      (typeElement as? KtUserType)?.referencedName == "JvmOverloads"
    }
  }

  private fun KtNamedFunction.isMemberOfAClassOrObject(): Boolean {
    val parentClass = this.getParentOfType<KtClass>(true)
    return parentClass != null && !parentClass.isInterface()
  }
}
