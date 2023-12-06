package org.example.detekt

import io.gitlab.arturbosch.detekt.api.Config
import io.gitlab.arturbosch.detekt.rules.KotlinCoreEnvironmentTest
import io.gitlab.arturbosch.detekt.test.compileAndLintWithContext
import io.kotest.matchers.collections.shouldHaveSize
import org.jetbrains.kotlin.cli.jvm.compiler.KotlinCoreEnvironment
import org.junit.jupiter.api.Test

@KotlinCoreEnvironmentTest
internal class JvmOverloadsAnnotationRuleTest(private val env: KotlinCoreEnvironment) {

    @Test
    fun `reports methods without @JvmOverloads annotation`() {
        val code = """
        class A {
          fun foo(a: Int = 1) {
          
          }
        }
        """
        val findings = JvmOverloadsAnnotationRule(Config.empty).compileAndLintWithContext(env, code)
        findings shouldHaveSize 1
    }

    @Test
    fun `doesn't report methods annotated with @JvmOverloads`() {
        val code = """
        class A {
          @JvmOverloads
          fun foo(a: Int = 1) {
          
          }
        }
        """
        val findings = JvmOverloadsAnnotationRule(Config.empty).compileAndLintWithContext(env, code)
        findings shouldHaveSize 0
    }

  @Test
  fun `doesn't report methods annotated with fully qualified @kotlinJvmJvmOverloads`() {
    val code = """
      class A {
        @kotlin.jvm.JvmOverloads
        fun foo(a: Int = 1) {

        }
      }
    """
    val findings = JvmOverloadsAnnotationRule(Config.empty).compileAndLintWithContext(env, code)
    findings shouldHaveSize 0
  }
}
