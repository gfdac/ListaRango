Desafio


    Crie uma tela para exibir o cardápio de cada um dos restaurantes: - ok
        Para cada restaurante, deve ser exibido os horários de funcionamento - ainda nao (fixo)
            as promoções ativas no momento - ainda nao
            o cardápio.- ok
        Para os produtos com promoção ativa, deve ser exibido o valor original e o valor promocional. - ainda nao apenas preco normal
        As promoções ativas e o valor promocional devem ser atualizados na interface, de acordo com o horário, sem a necessidade de recarregar ou reabrir a página.

Formato de horários

    É necessário tratar os campos que indicam horários de funcionamento.
    Os campos from e to possuem o formato HH:mm.
    Caso o campo to possua um horário anterior ao valor de from, deve-se considerar que o horário se estende até o horário contido em to do próximo dia.
    Por exemplo, se from for 19:00 e to for 02:00, o horário a ser considerado é das 19h do dia atual até às 02h do dia seguinte.
    O campo days guarda os dias da semana em que o horário é válido. Sendo Domingo o valor 1 e Sábado o valor 7. Os horários possuem intervalo mínimo de 15 minutos.



      desafios/problemas com os quais você se deparou durante a execução do projeto.
        ---
        agrupar os produtos por grupo
        atualizar informacoes da tela de forma individual (sem atualizar a gridview por exemplo)
        regras de horarios

        maneiras através das quais você pode melhorar a aplicação, seja em performance, estrutura ou padrões.
        talvez um KVO (Key Value Observer) para alterar os itens que sofreram com regras de horarios (restaurantes e itens de menu - promocoes)
        nao rodar nada de dados na main thread
        adicionar ACRA para verificar erros

        todas as intruções necessárias para que qualquer pessoa consiga rodar sua aplicação sem maiores problemas.
        Baixar o codigo
        abrir no android studio 3
        dar build e aguardar o gradlew
        rodar no android ou emulador
