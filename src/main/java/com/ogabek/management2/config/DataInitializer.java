package com.ogabek.management2.config;

import com.ogabek.management2.entity.*;
import com.ogabek.management2.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalTime;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final BranchRepository branchRepository;
    private final TeacherRepository teacherRepository;
    private final StudentRepository studentRepository;
    private final GroupRepository groupRepository;
    private final ScheduleRepository scheduleRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        if (userRepository.count() == 0) {
            log.info("Initializing sample data...");
            initializeData();
            log.info("Sample data initialization completed.");
        }
    }

    private void initializeData() {
        try {
            // Create admin user
            User adminUser = User.builder()
                    .username("admin")
                    .password(passwordEncoder.encode("admin123"))
                    .role(Role.ADMIN)
                    .enabled(true)
                    .build();
            userRepository.save(adminUser);

            // Create director user
            User directorUser = User.builder()
                    .username("director")
                    .password(passwordEncoder.encode("director123"))
                    .role(Role.DIRECTOR)
                    .enabled(true)
                    .build();
            userRepository.save(directorUser);

            // Create branch
            Branch mainBranch = Branch.builder()
                    .name("Main Branch")
                    .address("123 Education Street, Tashkent")
                    .phone("+998901234567")
                    .active(true)
                    .build();
            branchRepository.save(mainBranch);

            // Create teacher user
            User teacherUser = User.builder()
                    .username("teacher1")
                    .password(passwordEncoder.encode("teacher123"))
                    .role(Role.TEACHER)
                    .enabled(true)
                    .build();
            userRepository.save(teacherUser);

            // Create teacher
            Teacher teacher = Teacher.builder()
                    .firstName("John")
                    .lastName("Smith")
                    .phoneNumber("+998901234568")
                    .email("john.smith@example.com")
                    .salaryPercentage(BigDecimal.valueOf(25.0))
                    .branch(mainBranch)
                    .user(teacherUser)
                    .build();
            teacherRepository.save(teacher);

            // Create student user
            User studentUser = User.builder()
                    .username("student1")
                    .password(passwordEncoder.encode("student123"))
                    .role(Role.STUDENT)
                    .enabled(true)
                    .build();
            userRepository.save(studentUser);

            // Create student
            Student student = Student.builder()
                    .firstName("Alice")
                    .lastName("Johnson")
                    .phoneNumber("+998901234569")
                    .parentPhoneNumber("+998901234570")
                    .balance(BigDecimal.valueOf(500000))
                    .branch(mainBranch)
                    .user(studentUser)
                    .build();
            studentRepository.save(student);

            // Create group
            Group group = Group.builder()
                    .name("English Beginner A1")
                    .description("English language course for beginners")
                    .pricePerLesson(BigDecimal.valueOf(50000))
                    .status(GroupStatus.ACTIVE)
                    .branch(mainBranch)
                    .teacher(teacher)
                    .build();
            groupRepository.save(group);

            // Add student to group
            group.getStudents().add(student);
            groupRepository.save(group);

            // Create schedules for the group
            Schedule mondaySchedule = Schedule.builder()
                    .group(group)
                    .dayOfWeek(DayOfWeek.MONDAY)
                    .startTime(LocalTime.of(10, 0))
                    .endTime(LocalTime.of(11, 30))
                    .active(true)
                    .build();
            scheduleRepository.save(mondaySchedule);

            Schedule wednesdaySchedule = Schedule.builder()
                    .group(group)
                    .dayOfWeek(DayOfWeek.WEDNESDAY)
                    .startTime(LocalTime.of(10, 0))
                    .endTime(LocalTime.of(11, 30))
                    .active(true)
                    .build();
            scheduleRepository.save(wednesdaySchedule);

            Schedule fridaySchedule = Schedule.builder()
                    .group(group)
                    .dayOfWeek(DayOfWeek.FRIDAY)
                    .startTime(LocalTime.of(10, 0))
                    .endTime(LocalTime.of(11, 30))
                    .active(true)
                    .build();
            scheduleRepository.save(fridaySchedule);

            log.info("Created sample data:");
            log.info("- Admin user: admin/admin123");
            log.info("- Director user: director/director123");
            log.info("- Teacher user: teacher1/teacher123");
            log.info("- Student user: student1/student123");
            log.info("- Branch: Main Branch");
            log.info("- Group: English Beginner A1");
        } catch (Exception e) {
            log.error("Error initializing data: ", e);
        }
    }
}