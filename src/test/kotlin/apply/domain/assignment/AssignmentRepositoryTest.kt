package apply.domain.assignment

import apply.createAssignment
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import support.test.RepositoryTest

@RepositoryTest
class AssignmentRepositoryTest(
    private val assignmentRepository: AssignmentRepository
) {
    private val assignment = createAssignment(userId = 1L, missionId = 1L)

    @BeforeEach
    fun setUp() {
        assignmentRepository.save(assignment)
    }

    @Test
    fun `지원자와 과제에 해당하는 제출물의 존재 여부를 조회힌다`() {
        assertThat(assignmentRepository.existsByUserIdAndMissionId(assignment.userId, assignment.missionId)).isTrue
    }

    @Test
    fun `지원자와 과제에 해당하는 제출물을 반환한다`() {
        assertThat(assignmentRepository.findByUserIdAndMissionId(assignment.userId, assignment.missionId)).isNotNull
    }

    @Test
    fun `지원자의 모든 제출물을 조회한다`() {
        assignmentRepository.saveAll(
            listOf(
                createAssignment(userId = 1L, missionId = 2L),
                createAssignment(userId = 1L, missionId = 3L)
            )
        )
        assertThat(assignmentRepository.findAllByUserId(1L)).hasSize(3)
    }
}
