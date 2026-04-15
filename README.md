# 🎓 GradeSync

![Android](https://img.shields.io/badge/Android-3DDC84?style=for-the-badge&logo=android&logoColor=white)
![Java](https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)
![Room Database](https://img.shields.io/badge/Room_SQLite-003B57?style=for-the-badge&logo=sqlite&logoColor=white)

## 🚀 Sobre o Projeto

O **GradeSync** é um Sistema de Gestão Escolar nativo para Android focado em resolver um dos maiores gargalos logísticos das instituições de ensino: a **geração automatizada e inteligente de grades horárias**. 

Projetado para eliminar vieses de alocação, o motor do aplicativo distribui aulas de forma orgânica e justa, respeitando a carga horária e a disponibilidade de cada professor. É um MVP (Produto Mínimo Viável) robusto que une regras de negócios complexas a uma interface limpa, fluida e altamente responsiva, entregando uma experiência de nível corporativo na palma da mão.

---

## ✨ Funcionalidades Principais

* 📊 **Dashboard Dinâmico:** Painel de controle na Tela Inicial que exibe, em tempo real, métricas atualizadas sobre Turmas, Disciplinas e Professores cadastrados, com iconografia otimizada.
* 🎯 **Filtro de Visualização Pedagógica:** Tabela de horários dinâmica com suporte a múltiplas visões. Permite a **Visão Isolada** (grade específica de uma turma para alunos/professores) e o **Quadro Geral** (todas as turmas unificadas para a coordenação pedagógica).
* 🔄 **CRUD Completo e Intuitivo:** Gestão integral das entidades com usabilidade moderna. Suporte a exclusão rápida via *Swipe-to-Delete* e edição ágil com um único clique.
* 🧠 **Motor de Alocação Imparcial:** Algoritmo de geração de grade otimizado com a API de *Collections* do Java (`Collections.shuffle`), garantindo uma distribuição de aulas sem vícios de ordem alfabética ou de inserção.
* 📄 **Exportação de Relatórios Oficiais (PDF):** Geração de documentos PDF nativos utilizando a classe `PdfDocument` e o *Storage Access Framework*, imprimindo exatamente o recorte de tela selecionado no momento (seja de uma turma específica ou o quadro geral).
* 🛡️ **Tratamento de UX e Segurança:** Implementação de *Empty States* em listas vazias, programação defensiva para evitar falhas com banco de dados zerado e trava de **Estabilidade Visual**, garantindo a legibilidade da interface bloqueando conflitos com o Dark Mode do sistema operacional.

---

## 🛠 Tecnologias e Arquitetura

O projeto foi construído utilizando as seguintes tecnologias e padrões:

* **Linguagem:** Java (Android SDK).
* **Armazenamento Local:** Room Database (camada de abraçado sobre o SQLite) utilizando a arquitetura DAO (Data Access Object) para queries assíncronas e tipagem segura.
* **Padrões de Interface (UI/UX):** Utilização de `Toolbar` nativa personalizada, navegação via `Intents`, `Spinner` dinâmico via Adapters, `RecyclerViews` com `ItemTouchHelper` e estados visuais condicionais (Empty States).
* **Engenharia de Software:** Aplicação de conceitos de *Clean Code*, com a separação estrita da lógica de apresentação (Activities) e da lógica de negócios matemática, isolada em classes utilitárias como o `GeradorGradeHelper`.

---

## ⚙️ Como Executar

Siga os passos abaixo para clonar e testar o projeto localmente:

1. **Pré-requisitos:** Certifique-se de ter o [Android Studio](https://developer.android.com/studio) instalado na sua máquina.
2. **Clonar o Repositório:**
   ```bash
   git clone [https://github.com/SEU_USUARIO/GradeSync.git](https://github.com/SEU_USUARIO/GradeSync.git)
   ```