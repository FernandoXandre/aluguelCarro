package com.projeto_aluguelCarro.aluguelCarro.config;

import com.projeto_aluguelCarro.aluguelCarro.domain.Aluguel;
import com.projeto_aluguelCarro.aluguelCarro.domain.Carro;
import com.projeto_aluguelCarro.aluguelCarro.domain.Cliente;
import com.projeto_aluguelCarro.aluguelCarro.domain.Usuario;
import com.projeto_aluguelCarro.aluguelCarro.domain.enums.PerfilUsuario;
import com.projeto_aluguelCarro.aluguelCarro.domain.enums.StatusAluguel;
import com.projeto_aluguelCarro.aluguelCarro.repository.AluguelRepository;
import com.projeto_aluguelCarro.aluguelCarro.repository.CarroRepository;
import com.projeto_aluguelCarro.aluguelCarro.repository.ClienteRepository;
import com.projeto_aluguelCarro.aluguelCarro.repository.UsuarioRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;

@Component
public class DataSeeder implements CommandLineRunner {

    private final UsuarioRepository usuarios;
    private final CarroRepository carros;
    private final ClienteRepository clientes;
    private final AluguelRepository alugueis;
    private final BCryptPasswordEncoder encoder;

    public DataSeeder(UsuarioRepository usuarios, CarroRepository carros,
                      ClienteRepository clientes, AluguelRepository alugueis,
                      BCryptPasswordEncoder encoder) {
        this.usuarios = usuarios;
        this.carros   = carros;
        this.clientes = clientes;
        this.alugueis = alugueis;
        this.encoder  = encoder;
    }

    @Override
    public void run(String... args) {
            if (usuarios.count() > 0) return;

            // ── Usuários ──────────────────────────────────────────────
            Usuario admin = usuarios.save(Usuario.builder()
                    .username("admin")
                    .senha(encoder.encode("admin123"))
                    .perfil(PerfilUsuario.ADMIN)
                    .build());

            Usuario atendente = usuarios.save(Usuario.builder()
                    .username("joao.silva")
                    .senha(encoder.encode("atend123"))
                    .perfil(PerfilUsuario.ATENDENTE)
                    .build());

            usuarios.save(Usuario.builder()
                    .username("maria.santos")
                    .senha(encoder.encode("atend123"))
                    .perfil(PerfilUsuario.ATENDENTE)
                    .build());

            // ── Carros ────────────────────────────────────────────────
            Carro c1 = carros.save(Carro.builder().marca("Toyota").modelo("Corolla").placa("ABC-1234").ano(2022).categoria("Sedan").valorDiaria(new BigDecimal("180.00")).disponivel(true).build());
            Carro c2 = carros.save(Carro.builder().marca("Honda").modelo("Civic").placa("DEF-5678").ano(2023).categoria("Sedan").valorDiaria(new BigDecimal("200.00")).disponivel(true).build());
            Carro c3 = carros.save(Carro.builder().marca("Fiat").modelo("Pulse").placa("GHI-9012").ano(2023).categoria("SUV").valorDiaria(new BigDecimal("160.00")).disponivel(true).build());
            Carro c4 = carros.save(Carro.builder().marca("Hyundai").modelo("Creta").placa("JKL-3456").ano(2022).categoria("SUV").valorDiaria(new BigDecimal("220.00")).disponivel(true).build());
            Carro c5 = carros.save(Carro.builder().marca("Chevrolet").modelo("Onix").placa("MNO-7890").ano(2021).categoria("Econômico").valorDiaria(new BigDecimal("110.00")).disponivel(true).build());
            Carro c6 = carros.save(Carro.builder().marca("Volkswagen").modelo("T-Cross").placa("PQR-1234").ano(2023).categoria("SUV").valorDiaria(new BigDecimal("195.00")).disponivel(true).build());
            Carro c7 = carros.save(Carro.builder().marca("Jeep").modelo("Compass").placa("STU-5678").ano(2022).categoria("SUV").valorDiaria(new BigDecimal("280.00")).disponivel(true).build());
            Carro c8 = carros.save(Carro.builder().marca("BMW").modelo("320i").placa("VWX-9012").ano(2023).categoria("Luxo").valorDiaria(new BigDecimal("450.00")).disponivel(true).build());
            Carro c9 = carros.save(Carro.builder().marca("Renault").modelo("Kwid").placa("YZA-3456").ano(2021).categoria("Econômico").valorDiaria(new BigDecimal("90.00")).disponivel(true).build());
            Carro c10 = carros.save(Carro.builder().marca("Ford").modelo("Ranger").placa("BCD-7890").ano(2022).categoria("Utilitário").valorDiaria(new BigDecimal("320.00")).disponivel(true).build());

            // ── Clientes ──────────────────────────────────────────────
            Cliente cl1 = clientes.save(Cliente.builder().nome("Ana Carolina Ferreira").cpf("111.222.333-44").email("ana.ferreira@email.com").telefone("(11) 99001-1234").endereco("Rua das Flores, 120 — São Paulo/SP").cnh("12345678901").build());
            Cliente cl2 = clientes.save(Cliente.builder().nome("Bruno Henrique Lima").cpf("222.333.444-55").email("bruno.lima@email.com").telefone("(21) 98765-4321").endereco("Av. Atlântica, 500 — Rio de Janeiro/RJ").cnh("23456789012").build());
            Cliente cl3 = clientes.save(Cliente.builder().nome("Camila Oliveira Santos").cpf("333.444.555-66").email("camila.santos@email.com").telefone("(31) 97654-3210").endereco("Rua da Bahia, 80 — Belo Horizonte/MG").cnh("34567890123").build());
            Cliente cl4 = clientes.save(Cliente.builder().nome("Diego Martins Souza").cpf("444.555.666-77").email("diego.souza@email.com").telefone("(41) 96543-2109").endereco("Rua XV de Novembro, 300 — Curitiba/PR").cnh("45678901234").build());
            Cliente cl5 = clientes.save(Cliente.builder().nome("Fernanda Costa Ribeiro").cpf("555.666.777-88").email("fernanda.ribeiro@email.com").telefone("(51) 95432-1098").endereco("Av. Borges de Medeiros, 50 — Porto Alegre/RS").cnh("56789012345").build());
            Cliente cl6 = clientes.save(Cliente.builder().nome("Gabriel Alves Pereira").cpf("666.777.888-99").email("gabriel.pereira@email.com").telefone("(85) 94321-0987").endereco("Av. Beira Mar, 1000 — Fortaleza/CE").cnh("67890123456").build());
            Cliente cl7 = clientes.save(Cliente.builder().nome("Helena Nascimento Cruz").cpf("777.888.999-00").email("helena.cruz@email.com").telefone("(71) 93210-9876").endereco("Av. Sete de Setembro, 200 — Salvador/BA").cnh("78901234567").build());
            Cliente cl8 = clientes.save(Cliente.builder().nome("Igor Campos Teixeira").cpf("888.999.000-11").email("igor.teixeira@email.com").telefone("(62) 92109-8765").endereco("Av. Goiás, 45 — Goiânia/GO").cnh("89012345678").build());

            // ── Aluguéis concluídos (histórico 2026) ─────────────────
            alugueis.save(Aluguel.builder().cliente(cl1).carro(c5).usuario(atendente).dataInicio(LocalDate.of(2026, 1, 5)).dataFim(LocalDate.of(2026, 1, 10)).valorTotal(new BigDecimal("550.00")).status(StatusAluguel.CONCLUIDO).build());
            alugueis.save(Aluguel.builder().cliente(cl2).carro(c9).usuario(atendente).dataInicio(LocalDate.of(2026, 1, 15)).dataFim(LocalDate.of(2026, 1, 20)).valorTotal(new BigDecimal("450.00")).status(StatusAluguel.CONCLUIDO).build());
            alugueis.save(Aluguel.builder().cliente(cl3).carro(c1).usuario(admin).dataInicio(LocalDate.of(2026, 2, 3)).dataFim(LocalDate.of(2026, 2, 8)).valorTotal(new BigDecimal("900.00")).status(StatusAluguel.CONCLUIDO).build());
            alugueis.save(Aluguel.builder().cliente(cl4).carro(c3).usuario(atendente).dataInicio(LocalDate.of(2026, 2, 12)).dataFim(LocalDate.of(2026, 2, 17)).valorTotal(new BigDecimal("800.00")).status(StatusAluguel.CONCLUIDO).build());
            alugueis.save(Aluguel.builder().cliente(cl5).carro(c6).usuario(admin).dataInicio(LocalDate.of(2026, 3, 1)).dataFim(LocalDate.of(2026, 3, 7)).valorTotal(new BigDecimal("1170.00")).status(StatusAluguel.CONCLUIDO).build());
            alugueis.save(Aluguel.builder().cliente(cl6).carro(c2).usuario(atendente).dataInicio(LocalDate.of(2026, 3, 10)).dataFim(LocalDate.of(2026, 3, 15)).valorTotal(new BigDecimal("1000.00")).status(StatusAluguel.CONCLUIDO).build());
            alugueis.save(Aluguel.builder().cliente(cl7).carro(c8).usuario(admin).dataInicio(LocalDate.of(2026, 3, 20)).dataFim(LocalDate.of(2026, 3, 25)).valorTotal(new BigDecimal("2250.00")).status(StatusAluguel.CONCLUIDO).build());
            alugueis.save(Aluguel.builder().cliente(cl1).carro(c4).usuario(atendente).dataInicio(LocalDate.of(2026, 4, 2)).dataFim(LocalDate.of(2026, 4, 6)).valorTotal(new BigDecimal("880.00")).status(StatusAluguel.CONCLUIDO).build());
            alugueis.save(Aluguel.builder().cliente(cl8).carro(c10).usuario(admin).dataInicio(LocalDate.of(2026, 4, 14)).dataFim(LocalDate.of(2026, 4, 19)).valorTotal(new BigDecimal("1600.00")).status(StatusAluguel.CONCLUIDO).build());
            alugueis.save(Aluguel.builder().cliente(cl3).carro(c7).usuario(atendente).dataInicio(LocalDate.of(2026, 5, 5)).dataFim(LocalDate.of(2026, 5, 9)).valorTotal(new BigDecimal("1120.00")).status(StatusAluguel.CONCLUIDO).build());
            alugueis.save(Aluguel.builder().cliente(cl2).carro(c5).usuario(admin).dataInicio(LocalDate.of(2026, 5, 20)).dataFim(LocalDate.of(2026, 5, 25)).valorTotal(new BigDecimal("550.00")).status(StatusAluguel.CONCLUIDO).build());

            // ── Aluguéis cancelados ───────────────────────────────────
            alugueis.save(Aluguel.builder().cliente(cl4).carro(c9).usuario(atendente).dataInicio(LocalDate.of(2026, 2, 20)).dataFim(LocalDate.of(2026, 2, 25)).valorTotal(new BigDecimal("450.00")).status(StatusAluguel.CANCELADO).build());
            alugueis.save(Aluguel.builder().cliente(cl6).carro(c5).usuario(admin).dataInicio(LocalDate.of(2026, 4, 22)).dataFim(LocalDate.of(2026, 4, 27)).valorTotal(new BigDecimal("550.00")).status(StatusAluguel.CANCELADO).build());
            alugueis.save(Aluguel.builder().cliente(cl7).carro(c3).usuario(atendente).dataInicio(LocalDate.of(2026, 6, 1)).dataFim(LocalDate.of(2026, 6, 5)).valorTotal(new BigDecimal("640.00")).status(StatusAluguel.CANCELADO).build());

            // ── Aluguéis ativos (marcam carros como indisponíveis) ────
            c2.setDisponivel(false);
            carros.save(c2);
            alugueis.save(Aluguel.builder().cliente(cl5).carro(c2).usuario(admin).dataInicio(LocalDate.of(2026, 6, 18)).dataFim(LocalDate.of(2026, 6, 25)).valorTotal(new BigDecimal("1400.00")).status(StatusAluguel.ATIVO).build());

            c4.setDisponivel(false);
            carros.save(c4);
            alugueis.save(Aluguel.builder().cliente(cl8).carro(c4).usuario(atendente).dataInicio(LocalDate.of(2026, 6, 20)).dataFim(LocalDate.of(2026, 6, 28)).valorTotal(new BigDecimal("1760.00")).status(StatusAluguel.ATIVO).build());

            c7.setDisponivel(false);
            carros.save(c7);
            alugueis.save(Aluguel.builder().cliente(cl1).carro(c7).usuario(admin).dataInicio(LocalDate.of(2026, 6, 22)).dataFim(LocalDate.of(2026, 6, 30)).valorTotal(new BigDecimal("2240.00")).status(StatusAluguel.ATIVO).build());
    }
}
