CREATE TABLE IF NOT EXISTS students(
    id INT AUTO_INCREMENT NOT NULL PRIMARY KEY,
    name VARCHAR(50) NOT NULL,
    kana_name VARCHAR(50) NOT NULL,
    nickname VARCHAR(50),
    email VARCHAR(50) NOT NULL,
    area VARCHAR(50),
    age INT,
    sex VARCHAR(10),
    remark VARCHAR(255),
    deleted TINYINT NOT NULL DEFAULT 0
);

CREATE TABLE IF NOT EXISTS students_courses(
    id INT AUTO_INCREMENT NOT NULL PRIMARY KEY,
    student_id INT NOT NULL,
    course_name VARCHAR(50) NOT NULL,
    course_start_at TIMESTAMP,
    course_end_at TIMESTAMP
);

CREATE TABLE IF NOT EXISTS student_course_status(
    id INT AUTO_INCREMENT NOT NULL PRIMARY KEY,
    student_course_id INT NOT NULL,
    status ENUM('仮申込','本申込','受講中','受講終了')
    );
