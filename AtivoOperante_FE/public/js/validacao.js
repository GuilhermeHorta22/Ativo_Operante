//*** arquivo que vai ser utilizado para validar os campos no frontEnd ***

//função de formatação do cpf
function formatarCPF(cpf)
{
    cpf = cpf.replace(/\D/g, '');
    if(cpf.length > 11)
        cpf = cpf.slice(0, 11);
    cpf = cpf.replace(/(\d{3})(\d)/, '$1.$2');
    cpf = cpf.replace(/(\d{3})(\d)/, '$1.$2');
    cpf = cpf.replace(/(\d{3})(\d{1,2})$/, '$1-$2');
    return cpf;
}

//função que valida o cpf matematicamente
function validarCPF(cpf)
{
    cpf = cpf.replace(/[^\d]+/g, '');
    if(cpf.length !== 11 || /^(\d)\1{10}$/.test(cpf))
        return false;

    var soma = 0;
    var i;
    for(i = 0; i < 9; i++)
        soma += parseInt(cpf.charAt(i)) * (10 - i);
    var digito1 = 11 - (soma % 11);
    if(digito1 >= 10) 
        digito1 = 0;
    if(digito1 !== parseInt(cpf.charAt(9))) 
        return false;

    soma = 0;
    for(i = 0; i < 10; i++)
        soma += parseInt(cpf.charAt(i)) * (11 - i);

    var digito2 = 11 - (soma % 11);
    if(digito2 >= 10)
        digito2 = 0;

    return digito2 === parseInt(cpf.charAt(10));
}

//função que aplica a validação
function aplicarValidacaoCPF(idInput, idErro)
{
    var input = document.getElementById(idInput);
    var erro = document.getElementById(idErro);

    if(input)
    {
        input.addEventListener('input', () => {
            input.value = formatarCPF(input.value);
        });

        input.addEventListener('blur', () => {
            if(!validarCPF(input.value))
            {
                erro.textContent = 'CPF inválido.';
                input.classList.add('erro-input');
            }
            else
            {
                erro.textContent = '';
                input.classList.remove('erro-input');
            }
        });
    }
}

//chamando função de validação do cpf
document.addEventListener('DOMContentLoaded', () => {
    aplicarValidacaoCPF('cpf', 'erroCpf'); //na tela de cadastro
    aplicarValidacaoCPF('cpfLogin', 'erroCpfLogin'); //na tela de login
});

//função que valida o e-mail
function validarEmail(email)
{
    var valida1 = email.includes('@');
    var valida2 = email.endsWith('.com');

    if(!valida1 && !valida2)
        return 'O e-mail deve conter "@" e terminar com ".com".';
    if(!valida1)
        return 'O e-mail deve conter "@".';
    if(!valida2)
        return 'O e-mail deve terminar com ".com".';

    return ''; // válido
}

//função que aplica validação ao input do email
function aplicarValidacaoEmail(idInput, idErro)
{
    var input = document.getElementById(idInput);
    var erro = document.getElementById(idErro);

    if(input && erro)
    {
        input.addEventListener('input', function () {
            var mensagemErro = validarEmail(this.value);
    
            if(mensagemErro)
            {
                erro.textContent = mensagemErro;
                this.classList.add('erro-input');
            }
            else
            {
                erro.textContent = '';
                this.classList.remove('erro-input');
            }
        });
    }    
}

//chamando a função após carregamento da página
document.addEventListener('DOMContentLoaded', () => {
    aplicarValidacaoEmail('email', 'erroEmail');
    aplicarValidacaoEmail('emailLogin', 'erroEmailLogin');
});


//função que valida a senha
function validarSenha(senha)
{
    var valida = /^[0-9]+$/.test(senha);

    if(!valida)
        return 'A senha deve conter apenas números.';

    return ''; // se estiver ok
}

//função que aplica a validação da senha
function aplicarValidacaoSenha(idInput, idErro)
{
    var input = document.getElementById(idInput);
    var erro = document.getElementById(idErro);

    if(input && erro)
    {
        input.addEventListener('blur', function () {
            var mensagemErro = validarSenha(this.value);

            if(mensagemErro)
            {
                erro.textContent = mensagemErro;
                this.classList.add('erro-input');
            }
            else
            {
                erro.textContent = '';
                this.classList.remove('erro-input');
            }
        });
    }
}

//chamando a função após carregamento da página
document.addEventListener('DOMContentLoaded', () => {
    aplicarValidacaoSenha('senha', 'erroSenha');
    aplicarValidacaoSenha('senhaLogin', 'erroSenhaLogin');
});