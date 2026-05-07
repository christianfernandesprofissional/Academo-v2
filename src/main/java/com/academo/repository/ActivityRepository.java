package com.academo.repository;

import com.academo.controller.dtos.notification.NotificationDTO;
import com.academo.model.Activity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface ActivityRepository extends JpaRepository<Activity, Integer> {

    List<Activity> findAllByUserId(Integer userId);
    Page<Activity> findAllByUserId(Integer userId, Pageable pageable);
    Optional<Activity> findByIdAndUserId(Integer activityId, Integer userId);
    Boolean existsActivityByName(String activityName);
    List<Activity> findAllBySubjectId(Integer subjectId);
    Page<Activity> findAllBySubjectId(Integer subjectId, Pageable pageable);

    @Query("""
            SELECT a
            FROM Activity a
            WHERE a.user.id = :userId
              AND a.activityType.period.id = :periodId
            """)
    List<Activity> findAllByUserIdAndPeriodId(@Param("userId") Integer userId,
                                             @Param("periodId") Integer periodId);

    @Query("""
            SELECT a
            FROM Activity a
            WHERE a.user.id = :userId
              AND a.activityType.period.id = :periodId
            """)
    Page<Activity> findAllByUserIdAndPeriodId(@Param("userId") Integer userId,
                                             @Param("periodId") Integer periodId,
                                             Pageable pageable);

    @Query("""
            SELECT a
            FROM Activity a
            WHERE a.user.id = :userId
              AND a.activityType.period.id = :periodId
              AND a.activityType.name IN :activityTypeNames
            """)
    Page<Activity> findAllByUserIdAndPeriodIdAndActivityTypeNames(@Param("userId") Integer userId,
                                                                 @Param("periodId") Integer periodId,
                                                                 @Param("activityTypeNames") List<String> activityTypeNames,
                                                                 Pageable pageable);

    @Query(nativeQuery = true, value = """
            SELECT\s
            \ttu.email,
            \tjson_agg(
            \t\tjson_build_object(
            \t\t\t'name', ta.name,
            \t\t\t'description', ta.description,
            \t\t\t'subject', ts.name,
            \t\t\t'activityDate', ta.activity_date,
            \t\t\t'notificationDate', ta.notification_date
            \t\t)
            \t) as activities
            FROM
            \ttb_activities ta
            INNER JOIN
            \ttb_subjects ts
            ON
            \tta.subject_id = ts.id
            INNER JOIN
            \ttb_users tu
            ON
            \tta.user_id = tu.id
            WHERE
            \tta.notification_date = :date
            GROUP BY
            \ttu.email""")
    List<NotificationDTO> searchNotificationByDate(@Param("date") LocalDate date);
}
