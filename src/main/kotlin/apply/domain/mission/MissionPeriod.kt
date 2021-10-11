package apply.domain.mission

import java.time.LocalDateTime
import javax.persistence.Column
import javax.persistence.Embeddable

fun LocalDateTime.isBefore(period: MissionPeriod): Boolean = this < period.startDateTime

fun LocalDateTime.isAfter(period: MissionPeriod): Boolean = this > period.endDateTime

fun LocalDateTime.isBetween(period: MissionPeriod): Boolean = period.contains(this)

@Embeddable
data class MissionPeriod(
    @Column(nullable = false)
    val startDateTime: LocalDateTime,

    @Column(nullable = false)
    val endDateTime: LocalDateTime
) {
    fun contains(value: LocalDateTime): Boolean = (startDateTime..endDateTime).contains(value)

    init {
        require(endDateTime >= startDateTime) { "시작 일시는 종료 일시보다 이후일 수 없습니다." }
    }
}
