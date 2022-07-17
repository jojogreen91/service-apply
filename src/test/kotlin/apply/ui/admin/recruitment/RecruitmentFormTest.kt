package apply.ui.admin.recruitment

import apply.createRecruitmentData
import apply.createRecruitmentForm
import apply.createRecruitmentItemData
import com.vaadin.flow.component.UI
import io.kotest.core.spec.style.FreeSpec
import io.kotest.inspectors.forAll
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockkStatic

internal class RecruitmentFormTest : FreeSpec({

    /**
     * Static classes are mocked for the following reasons:
     * [issue](https://github.com/vaadin/vaadin-date-picker-flow/issues/262)
     */
    beforeEach {
        mockkStatic("com.vaadin.flow.component.UI")
        every { UI.getCurrent() }.returns(UI())
    }

    "유효한 값을 입력하는 경우" {
        val actual = createRecruitmentForm().bindOrNull()
        actual shouldBe createRecruitmentData()
    }

    "잘못된 값을 입력한 경우" {
        val actual = createRecruitmentForm(title = "").bindOrNull()
        actual.shouldBeNull()
    }

    "공개 여부 값이 설정되어 있는지 확인" {
        listOf(
            true,
            false
        ).forAll {
            val actual = createRecruitmentForm(hidden = it).bindOrNull()
            actual.shouldNotBeNull()
            actual!!.hidden shouldBe it
        }
    }

    "양식에 값을 채울 수 있다" {
        val data = createRecruitmentData(id = 1L, recruitmentItems = listOf(createRecruitmentItemData(id = 1L)))
        val form = createRecruitmentForm()
        form.fill(data)
        form.bindOrNull() shouldBe createRecruitmentData(
            id = 1L,
            recruitmentItems = listOf(createRecruitmentItemData(id = 1L))
        )
    }
})
