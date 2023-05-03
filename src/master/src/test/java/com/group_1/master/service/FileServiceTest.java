package com.group_1.master.service;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * com.group_1.master.service
 * Created by NhatLinh - 19127652
 * Date 5/3/2023 - 3:07 PM
 * Description: ...
 */
@ExtendWith(MockitoExtension.class)
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
public class FileServiceTest {

//    @InjectMocks
//    private FileService service;
//
//    @Mock
//    private FileService repository;
//
//    @Test
//    void should_save_one_student() {
//        // Arrange
//
//        final var studentToSave = Student.builder().name("Mary Jane").age(25).build();
//        when(repository.save(any(Student.class))).thenReturn(studentToSave);
//
//        // Act
//        final var actual = service.saveOneStudent(new Student());
//
//        // Assert
//        assertThat(actual).usingRecursiveComparison().isEqualTo(studentToSave);
//        verify(repository, times(1)).save(any(Student.class));
//        verifyNoMoreInteractions(repository);
//    }


}
