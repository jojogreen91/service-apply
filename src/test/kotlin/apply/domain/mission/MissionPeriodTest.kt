package apply.domain.mission

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FreeSpec
import java.time.LocalDateTime

class MissionPeriodTest : FreeSpec({

    val now: LocalDateTime = LocalDateTime.now()

    "시작 일시는 종료 일시보다 이후일 수 없다" {
        shouldThrow<IllegalArgumentException> { MissionPeriod(now, now.minusDays(1L)) }
    }
})
