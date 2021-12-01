package ru.gov.mcx.gispz.subject.service;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.gov.mcx.gispz.auth.AuthenticationBean;
import ru.gov.mcx.gispz.commons.GISPZException;
import ru.gov.mcx.gispz.commons.GISPZValidationException;
import ru.gov.mcx.gispz.commons.json.ErrorResponse;
import ru.gov.mcx.gispz.fias.FiasProvider;
import ru.gov.mcx.gispz.model.nci.Country;
import ru.gov.mcx.gispz.model.nci.DocumentIdentType;
import ru.gov.mcx.gispz.model.nci.Opf;
import ru.gov.mcx.gispz.model.subject.*;
import ru.gov.mcx.gispz.subject.SubjectProvider;
import ru.gov.mcx.gispz.subject.SubjectRequest;

import javax.persistence.PersistenceException;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;


/**
 * Организации
 *
 * @author mterehov
 */
@RestController
@RequestMapping("/api/subject")
@Tag(name = "Subject", description = "Сервис для работы с субъектами")
public class SubjectController {
    /** Logger */
    private static final Logger log = LogManager.getLogger();

    @Autowired
    private SubjectProvider provider;
    @Autowired
    private FiasProvider fias;

    @Autowired
    AuthenticationBean authBean;


    public SubjectController() {
    }


    /**
     * Список ОПФ
     */
    @GetMapping(value = "/opf", produces = APPLICATION_JSON_VALUE)
    @Operation(description = "Получить список ОПФ")
    public ResponseEntity<Page<Opf>> getOpfList() {
        return ResponseEntity.ok(new PageImpl<>(provider.getOpfs()));
    }


    /**
     * Вид документа удостоверяющего личность
     */
    @GetMapping(value = "/document_ident_type", produces = APPLICATION_JSON_VALUE)
    @Operation(description = "Вид документа удостоверяющего личность")
    public ResponseEntity<Page<DocumentIdentType>> getDocumentIdentTypeList() {
        return ResponseEntity.ok(new PageImpl<>(provider.getDocumentIdentTypes()));
    }


    /**
     * Страны мира (ОКСМ)
     */
    @GetMapping(value = "/country", produces = APPLICATION_JSON_VALUE)
    @Operation(description = "Общероссийский классификатор стран мира (ОКСМ)")
    public ResponseEntity<Page<Country>> getCountry() {
        return ResponseEntity.ok(new PageImpl<>(provider.getCountries()));
    }


    /**
     * Поиск организации по ID
     *
     * @param subjectId -
     */
    @GetMapping("/{subject_id}")
    @Operation(description = "Организация по ID", responses = {
        @ApiResponse(responseCode = "200", description = "ОК"),
        @ApiResponse(responseCode = "500", description = "Во время выполнения запроса произошла ошибка")
    })
    public ResponseEntity<Subject> getById(@PathVariable(name = "subject_id") Long subjectId) {
        return provider.getById(subjectId)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.noContent().build());
    }


    /**
     * Поиск организации по ID с подраздлениями
     *
     * @param subjectId -
     */
    @GetMapping("/withDivisions/{subject_id}")
    @Operation(description = "Организация по ID", responses = {
        @ApiResponse(responseCode = "200", description = "ОК"),
        @ApiResponse(responseCode = "500", description = "Во время выполнения запроса произошла ошибка")
    })
    public ResponseEntity<Subject> getByIdWithDivisions(@PathVariable(name = "subject_id") Long subjectId) {
        return provider.getByIdWithDivisions(subjectId)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.noContent().build());
    }


    /** Поиск организации с загрузкой подразделений */
    @PostMapping(value = "/findWithDivisions", produces = APPLICATION_JSON_VALUE)
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "ОК"),
        @ApiResponse(responseCode = "500", description = "Во время выполнения запроса произошла ошибка")
    })
    @Operation(description = "Поиск организации по критериям с загрузкой подразделений")
    public ResponseEntity<Page<SubjectItem>> findWithDivisions(SubjectRequest request) {
        return ResponseEntity.ok(provider.findWithDivisions(request != null ? request : new SubjectRequest()));
    }

    /**
     * Поиск организации
     */
    @PostMapping(value = "/subjects", produces = APPLICATION_JSON_VALUE)
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "ОК"),
        @ApiResponse(responseCode = "500", description = "Во время выполнения запроса произошла ошибка")
    })
    @Operation(description = "Поиск организации по критериям")
    public ResponseEntity<Page<SubjectItem>> find(SubjectRequest request) {
        return ResponseEntity.ok(provider.find(request != null ? request : new SubjectRequest()));
    }


    /**
     * Создать запись об организации
     *
     * @param subject -
     */
    @PostMapping("/create")
    @Operation(description = "Создать запись об организации", responses = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "500", description = "Во время выполнения запроса произошла ошибка")
    })
    public ResponseEntity<Subject> create(@RequestBody Subject subject) throws GISPZException {
        if (subject == null)
            throw GISPZException.builder()
                .code("GISPZ-125")
                .build();

        return provider.persist(subject).map(ResponseEntity::ok).orElse(ResponseEntity.badRequest().build());
    }


    /**
     * Изменить запись об организации
     *
     * @param subject -
     */
    @PostMapping("/update")
    @Operation(description = "Изменить запись об организации", responses = {
        @ApiResponse(responseCode = "200", description = "OK"),
        @ApiResponse(responseCode = "500", description = "Во время выполнения запроса произошла ошибка")
    })
    public ResponseEntity<Subject> update(@RequestBody Subject subject) throws GISPZException {
        if (subject == null)
            throw GISPZException.builder()
                .code("GISPZ-125")
                .build();

        return provider.update(subject).map(ResponseEntity::ok).orElse(ResponseEntity.badRequest().build());
    }


    /**
     * Изменить запись об организации
     *
     * @param subject -
     */
    @PostMapping("/delete")
    @Operation(description = "Удалить запись об организации", responses = {
        @ApiResponse(responseCode = "200", description = "OK"),
        @ApiResponse(responseCode = "500", description = "Во время выполнения запроса произошла ошибка")
    })
    public ResponseEntity<Subject> delete(@RequestBody Subject subject) throws GISPZException {
        if (subject == null)
            throw GISPZException.builder()
                .code("GISPZ-125")
                .build();

        provider.delete(subject);

        return ResponseEntity.ok().build();
    }


    /**
     * Обрабочики ошибок
     *
     * @param e {@link PersistenceException}
     * @return ResponseEntity
     */
    @ExceptionHandler(PersistenceException.class)
    public ResponseEntity<ErrorResponse> handleException(Exception e) {
        // трассировка для ошибки
        log.fatal("", e);

        Throwable t = ExceptionUtils.getRootCause(e);

        if (t instanceof GISPZException)
            return handleException((GISPZException) t);

        // Для всех остальных случаев надо обязательно внести в лог причину
        log.error(t);

        return ResponseEntity
            .badRequest()
            .body(
                ErrorResponse.builder()
                    .cause(e)
                    .message(t.getMessage())
                    .status(HttpStatus.BAD_REQUEST)
                    .build()
            );
    }


    /**
     * Обрабочики ошибок
     *
     * @param e {@link GISPZException}
     * @return ResponseEntity
     */
    @ExceptionHandler(GISPZException.class)
    public ResponseEntity<ErrorResponse> handleException(GISPZException e) {
        // трассировка для ошибки
        log.fatal("", e);

        return ResponseEntity
            .badRequest()
            .body(
                ErrorResponse.builder()
                    .message(e.getMessage())
                    .status(HttpStatus.BAD_REQUEST)
                    .build()
            );
    }


    /**
     * Обрабочики ошибок валидации
     *
     * @param e {@link GISPZValidationException}
     * @return ResponseEntity
     */
    @ExceptionHandler(GISPZValidationException.class)
    public ResponseEntity<ErrorResponse> handleException(GISPZValidationException e) {
        // трассировка для ошибки
        log.trace(e);

        return ResponseEntity
            .badRequest()
            .body(
                ErrorResponse.builder()
                    .messages(e.getMessages())
                    .status(HttpStatus.BAD_REQUEST)
                    .build()
            );
    }
}
