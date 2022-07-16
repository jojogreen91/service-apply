package apply.ui.api

import apply.config.RestDocsConfiguration
import apply.createUser
import apply.security.LoginFailedException
import apply.security.LoginUser
import apply.security.LoginUserResolver
import com.fasterxml.jackson.databind.ObjectMapper
import com.ninjasquad.springmockk.MockkBean
import io.kotest.core.spec.style.FreeSpec
import io.kotest.core.test.TestCase
import io.kotest.core.test.TestResult
import io.mockk.every
import io.mockk.slot
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Import
import org.springframework.core.MethodParameter
import org.springframework.http.HttpHeaders
import org.springframework.restdocs.ManualRestDocumentation
import org.springframework.restdocs.RestDocumentationExtension
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation
import org.springframework.test.context.web.WebAppConfiguration
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.result.MockMvcResultHandlers
import org.springframework.test.web.servlet.setup.DefaultMockMvcBuilder
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.web.context.WebApplicationContext
import org.springframework.web.context.request.NativeWebRequest
import org.springframework.web.filter.CharacterEncodingFilter
import support.test.TestEnvironment

@Import(RestDocsConfiguration::class)
@ExtendWith(RestDocumentationExtension::class)
@TestEnvironment
@WebAppConfiguration
abstract class RestControllerTest(body: FreeSpec.() -> Unit = {}) : FreeSpec() {
    @MockkBean
    private lateinit var loginUserResolver: LoginUserResolver

    @Autowired
    lateinit var objectMapper: ObjectMapper

    @Autowired
    private lateinit var webApplicationContext: WebApplicationContext

    private val restDocumentation: ManualRestDocumentation = ManualRestDocumentation()

    lateinit var mockMvc: MockMvc

    override suspend fun beforeEach(testCase: TestCase) {
        super.beforeEach(testCase)
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
            .addFilter<DefaultMockMvcBuilder>(CharacterEncodingFilter("UTF-8", true))
            .alwaysDo<DefaultMockMvcBuilder>(MockMvcResultHandlers.print())
            .apply<DefaultMockMvcBuilder>(MockMvcRestDocumentation.documentationConfiguration(restDocumentation))
            .build()
        restDocumentation.beforeTest(this.javaClass, "RestControllerTest")

        loginUserResolver.also {
            slot<MethodParameter>().also { slot ->
                every { it.supportsParameter(capture(slot)) } answers {
                    slot.captured.hasParameterAnnotation(LoginUser::class.java)
                }
            }
            slot<NativeWebRequest>().also { slot ->
                every { it.resolveArgument(any(), any(), capture(slot), any()) } answers {
                    val hasToken = slot.captured.getHeader(HttpHeaders.AUTHORIZATION)?.contains("Bearer")
                    if (hasToken != true) {
                        throw LoginFailedException()
                    }
                    createUser()
                }
            }
        }
    }

    override suspend fun afterEach(testCase: TestCase, result: TestResult) {
        super.afterEach(testCase, result)
        restDocumentation.afterTest()
    }
}
