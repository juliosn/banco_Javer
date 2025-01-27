import com.julioneves.requisicao.RequisicaoApplication;
import com.julioneves.requisicao.web.dto.ClienteCreateDto;
import com.julioneves.requisicao.web.dto.ClienteResponseDto;
import com.julioneves.requisicao.web.dto.ClienteUpdateDto;
import com.julioneves.requisicao.web.exception.ErrorMessage;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = RequisicaoApplication.class)
@Sql(scripts = "classpath:/sql/clientes/clientes-insert.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = "classpath:/sql/clientes/clientes-delete.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class ClienteIT {

    @Autowired
    WebTestClient webTestClient;


    @Test
    public void addCliente_ComDadosValidos_RetornaStatus201() {
        ClienteResponseDto responseBody = webTestClient
                .post()
                .uri("api/v1/conta/clientes")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new ClienteCreateDto("Júlio", 30031400000000L, true, 1000f))
                .exchange()
                .expectStatus().isCreated()
                .expectBody(ClienteResponseDto.class)
                .returnResult().getResponseBody();

        assertThat(responseBody).isNotNull();
        assertThat(responseBody.getNome()).isEqualTo("Júlio");
        assertThat(responseBody.getTelefone()).isEqualTo(30031400000000L);
        assertThat(responseBody.getCorrentista()).isTrue();
        assertThat(responseBody.getSaldoCc()).isEqualTo(1000f);

    }

    @Test
    public void addCliente_ComDadosInvalidos_RetornaStatus400() {
        webTestClient
                .post()
                .uri("api/v1/conta/clientes")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new ClienteCreateDto("", 30031400000000L, true, 1000f)) // Dados inválidos (nome vazio)
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody(ErrorMessage.class)
                .consumeWith(response -> {
                    String errorMessage = response.getResponseBody().getMessage();
                    assertThat(errorMessage).contains("Dados inválidos fornecidos para a criação do cliente.");
                });
    }

    @Test
    public void getClienteById_comIdValido_RetornaStatus200() {
        ClienteResponseDto clienteCriado = webTestClient
                .post()
                .uri("api/v1/conta/clientes")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new ClienteCreateDto("Júlio", 30031400000000L, true, 1000f))
                .exchange()
                .expectStatus().isCreated()
                .expectBody(ClienteResponseDto.class)
                .returnResult().getResponseBody();

        assertThat(clienteCriado).isNotNull();
        assertThat(clienteCriado.getNome()).isEqualTo("Júlio");
        assertThat(clienteCriado.getTelefone()).isEqualTo(30031400000000L);
        assertThat(clienteCriado.getCorrentista()).isTrue();
        assertThat(clienteCriado.getSaldoCc()).isEqualTo(1000f);


        ClienteResponseDto responseBody = webTestClient
                .get()
                .uri("api/v1/conta/clientes/1")
                .exchange()
                .expectStatus().isOk()
                .expectBody(ClienteResponseDto.class)
                .returnResult().getResponseBody();

        assertThat(responseBody).isNotNull();
        assertThat(responseBody.getNome()).isEqualTo("Novo Nome");
    }

    @Test
    public void getClienteById_comIdInvalido_RetornaStatus404() {
        ErrorMessage responseBody = webTestClient
                .get()
                .uri("api/v1/conta/clientes/100")
                .exchange()
                .expectStatus().isNotFound()
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        assertThat(responseBody).isNotNull();
        assertThat(responseBody.getMessage()).contains("Cliente com ID 100 não encontrado");
    }

    @Test
    public void deleteCliente_ComIdValido_RetornaStatus204() {
        ClienteResponseDto responseBody = webTestClient
                .post()
                .uri("api/v1/conta/clientes")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new ClienteCreateDto("Júlio", 30031400000000L, true, 1000f))
                .exchange()
                .expectStatus().isCreated()
                .expectBody(ClienteResponseDto.class)
                .returnResult().getResponseBody();

        assertThat(responseBody).isNotNull();
        assertThat(responseBody.getNome()).isEqualTo("Júlio");
        assertThat(responseBody.getTelefone()).isEqualTo(30031400000000L);
        assertThat(responseBody.getCorrentista()).isTrue();
        assertThat(responseBody.getSaldoCc()).isEqualTo(1000f);


        webTestClient
                .delete()
                .uri("api/v1/conta/clientes/2")
                .exchange()
                .expectStatus().isNoContent();
    }

    @Test
    public void deleteCliente_ComIdInvalido_RetornaStatus404() {
        webTestClient
                .delete()
                .uri("api/v1/conta/clientes/11")
                .exchange()
                .expectStatus().isNotFound()
                .expectBody(ErrorMessage.class)
                .consumeWith(response -> {
                    String errorMessage = response.getResponseBody().getMessage();
                    assertThat(errorMessage).contains("Cliente com ID 11 não encontrado");
                });
    }


    @Test
    public void updateCliente_ComDadosValidos_RetornaStatus200() {
        ClienteResponseDto clienteCriado = webTestClient
                .post()
                .uri("api/v1/conta/clientes")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new ClienteCreateDto("Júlio", 30031400000000L, true, 1000f))
                .exchange()
                .expectStatus().isCreated()
                .expectBody(ClienteResponseDto.class)
                .returnResult().getResponseBody();

        assertThat(clienteCriado).isNotNull();
        assertThat(clienteCriado.getNome()).isEqualTo("Júlio");
        assertThat(clienteCriado.getTelefone()).isEqualTo(30031400000000L);
        assertThat(clienteCriado.getCorrentista()).isTrue();
        assertThat(clienteCriado.getSaldoCc()).isEqualTo(1000f);


        ClienteResponseDto responseBody = webTestClient
                .put()
                .uri("api/v1/conta/clientes/1")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new ClienteUpdateDto("Novo Nome", 30031400000001L, true, 2000f)) // Dados atualizados
                .exchange()
                .expectStatus().isOk()
                .expectBody(ClienteResponseDto.class)
                .returnResult().getResponseBody();

        assertThat(responseBody).isNotNull();
        assertThat(responseBody.getNome()).isEqualTo("Novo Nome");
        assertThat(responseBody.getTelefone()).isEqualTo(30031400000001L);
        assertThat(responseBody.getCorrentista()).isTrue();
        assertThat(responseBody.getSaldoCc()).isEqualTo(2000f);
    }

    @Test
    public void updateCliente_ComDadosInvalidos_RetornaStatus400() {
        ClienteResponseDto clienteCriado = webTestClient
                .post()
                .uri("api/v1/conta/clientes")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new ClienteCreateDto("Júlio", 30031400000000L, true, 1000f))
                .exchange()
                .expectStatus().isCreated()
                .expectBody(ClienteResponseDto.class)
                .returnResult().getResponseBody();

        assertThat(clienteCriado).isNotNull();
        assertThat(clienteCriado.getNome()).isEqualTo("Júlio");
        assertThat(clienteCriado.getTelefone()).isEqualTo(30031400000000L);
        assertThat(clienteCriado.getCorrentista()).isTrue();
        assertThat(clienteCriado.getSaldoCc()).isEqualTo(1000f);


        webTestClient
                .put()
                .uri("api/v1/conta/clientes/1")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new ClienteUpdateDto("", 30031400000001L, true, 2000f))
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody(ErrorMessage.class)
                .consumeWith(response -> {
                    String errorMessage = response.getResponseBody().getMessage();
                    assertThat(errorMessage).contains("Dados inválidos fornecidos para atualização do cliente.");
                });
    }

    @Test
    public void updateCliente_IdInvalido_RetornaStatus404() {
        webTestClient
                .put()
                .uri("api/v1/conta/clientes/11")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new ClienteUpdateDto("GOku", 30031400000001L, true, 2000f)) // Dados inválidos (nome vazio)
                .exchange()
                .expectStatus().isNotFound()
                .expectBody(ErrorMessage.class)
                .consumeWith(response -> {
                    String errorMessage = response.getResponseBody().getMessage();
                    assertThat(errorMessage).contains("Cliente com ID 11 não encontrado");
                });
    }

    @Test
    public void getAllClientes_DeveRetornarListaDeClientesComStatus200() {
        List<Map> responseBody = webTestClient
                .get()
                .uri("/api/v1/conta/clientes")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(Map.class)
                .returnResult()
                .getResponseBody();

        assertThat(responseBody).isNotNull();
    }

}

