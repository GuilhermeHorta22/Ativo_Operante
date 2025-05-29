// Script para funcionalidades gerais do sistema
document.addEventListener('DOMContentLoaded', () => {
    // Verificar se está em uma página que precisa de autenticação
    const currentPage = window.location.pathname.split('/').pop();

    if (currentPage === 'home.html' || currentPage === 'adm.html') {
        verificarAutenticacao();
    }

    // Se estiver na página de denúncias (home.html)
    if (currentPage === 'home.html') {
        inicializarPaginaDenuncias();
    }

    // Se estiver na página de admin (adm.html)
    if (currentPage === 'adm.html') {
        inicializarPaginaAdmin();
    }
});

// Verificar autenticação geral
function verificarAutenticacao() {
    const token = localStorage.getItem('token');
    const nivel = localStorage.getItem('nivel');

    if (!token) {
        alert('Você precisa estar logado para acessar esta página.');
        window.location.href = 'index.html';
        return false;
    }

    return true;
}

// Função para fazer logout
function logout() {
    if (confirm('Deseja realmente sair?')) {
        localStorage.clear();
        window.location.href = 'index.html';
    }
}

// Função para obter token
function getAuthToken() {
    return localStorage.getItem('token');
}

// Função para fazer requisições autenticadas
async function makeAuthenticatedRequest(url, options = {}) {
    const token = getAuthToken();

    if (!token) {
        throw new Error('Token de autenticação não encontrado');
    }

    const defaultOptions = {
        headers: {
            'Authorization': `Bearer ${token}`,
            'Content-Type': 'application/json',
            ...options.headers
        }
    };

    const finalOptions = { ...defaultOptions, ...options };

    try {
        const response = await fetch(url, finalOptions);

        if (response.status === 401 || response.status === 403) {
            alert('Sua sessão expirou. Por favor, faça login novamente.');
            localStorage.clear();
            window.location.href = 'index.html';
            return null;
        }

        return response;
    } catch (error) {
        console.error('Erro na requisição autenticada:', error);
        throw error;
    }
}

// Função para extrair dados do token JWT
function decodeJWTToken(token) {
    try {
        const payload = JSON.parse(atob(token.split('.')[1]));
        return payload;
    } catch (error) {
        console.error('Erro ao decodificar token:', error);
        return null;
    }
}

// Função para obter ID do usuário do token
function getUserIdFromToken() {
    const token = getAuthToken();
    if (!token) return null;

    const payload = decodeJWTToken(token);
    return payload ? payload.id : null;
}

// Função para obter nível do usuário do token
function getUserLevelFromToken() {
    const token = getAuthToken();
    if (!token) return null;

    const payload = decodeJWTToken(token);
    return payload ? payload.nivel : null;
}

// Inicializar página de denúncias (home.html)
async function inicializarPaginaDenuncias() {
    const nivel = localStorage.getItem('nivel');

    if (nivel !== '2') {
        alert('Acesso negado. Esta área é apenas para cidadãos.');
        localStorage.clear();
        window.location.href = 'index.html';
        return;
    }

    try {
        // Carregar dados necessários
        await Promise.all([
            carregarOrgaosSelect(),
            carregarTiposProblemaSelect()
        ]);

        // Configurar eventos
        configurarEventosDenuncia();

        console.log('Página de denúncias inicializada com sucesso');
    } catch (error) {
        console.error('Erro ao inicializar página de denúncias:', error);
        alert('Erro ao carregar dados iniciais.');
    }
}

// Inicializar página de admin (adm.html)
async function inicializarPaginaAdmin() {
    const nivel = localStorage.getItem('nivel');

    if (nivel !== '1') {
        alert('Acesso negado. Esta área é apenas para administradores.');
        localStorage.clear();
        window.location.href = 'index.html';
        return;
    }

    try {
        console.log('Página de admin inicializada com sucesso');
    } catch (error) {
        console.error('Erro ao inicializar página de admin:', error);
        alert('Erro ao carregar dados administrativos.');
    }
}

// Carregar órgãos para select
async function carregarOrgaosSelect() {
    try {
        const response = await fetch('http://localhost:8080/apis/admin/orgaos');

        if (!response.ok) {
            throw new Error('Erro ao carregar órgãos');
        }

        const orgaos = await response.json();
        const select = document.getElementById('orgaoSelect');

        if (select) {
            // Limpar opções existentes (mantém apenas a primeira)
            select.options.length = 1;

            orgaos.forEach(orgao => {
                const option = document.createElement('option');
                option.value = orgao.id;
                option.textContent = orgao.nome;
                option.classList.add('bg-[#1d2a34]');
                select.appendChild(option);
            });
        }
    } catch (error) {
        console.error('Erro ao carregar órgãos:', error);
    }
}

// Carregar tipos de problema para select
async function carregarTiposProblemaSelect() {
    try {
        const response = await fetch('http://localhost:8080/apis/admin/tipo');

        if (!response.ok) {
            throw new Error('Erro ao carregar tipos de problema');
        }

        const tipos = await response.json();
        const select = document.getElementById('tipoProblemaSelect');

        if (select) {
            // Limpar opções existentes (mantém apenas a primeira)
            select.options.length = 1;

            tipos.forEach(tipo => {
                const option = document.createElement('option');
                option.value = tipo.id;
                option.textContent = tipo.nome;
                option.classList.add('bg-[#1d2a34]');
                select.appendChild(option);
            });
        }
    } catch (error) {
        console.error('Erro ao carregar tipos de problema:', error);
    }
}

// Configurar eventos para formulário de denúncia
function configurarEventosDenuncia() {
    const form = document.getElementById('denunciaForm');

    if (form) {
        form.addEventListener('submit', handleDenunciaSubmit);
    }

    // Configurar botão de suporte se existir
    const suporteBtn = document.getElementById('contatoSuporte');
    if (suporteBtn) {
        suporteBtn.addEventListener('click', () => {
            alert('Para suporte, entre em contato pelo email: suporte@ativooperante.com');
        });
    }
}

// Manipular envio de denúncia
async function handleDenunciaSubmit(e) {
    e.preventDefault();

    const userId = getUserIdFromToken();
    if (!userId) {
        alert('Erro ao obter dados do usuário. Faça login novamente.');
        return;
    }

    // Coletar dados do formulário
    const titulo = document.getElementById('denunciaTitulo').value.trim();
    const texto = document.getElementById('denunciaTexto').value.trim();
    const urgencia = parseInt(document.getElementById('denunciaUrgencia').value);
    const orgaoId = parseInt(document.getElementById('orgaoSelect').value);
    const tipoId = parseInt(document.getElementById('tipoProblemaSelect').value);

    // Validações
    if (!titulo || !texto) {
        alert('Por favor, preencha todos os campos obrigatórios.');
        return;
    }

    if (isNaN(urgencia) || urgencia < 1 || urgencia > 5) {
        alert('A urgência deve ser um número entre 1 e 5.');
        return;
    }

    if (isNaN(orgaoId) || isNaN(tipoId)) {
        alert('Por favor, selecione o órgão e o tipo de problema.');
        return;
    }

    const denuncia = {
        titulo,
        texto,
        urgencia,
        data: new Date().toISOString().split('T')[0],
        orgao: { id: orgaoId },
        tipo: { id: tipoId },
        usuario: { id: userId }
    };

    try {
        const response = await makeAuthenticatedRequest('http://localhost:8080/apis/cidadao/denuncia', {
            method: 'POST',
            body: JSON.stringify(denuncia)
        });

        if (response && response.ok) {
            alert('Denúncia enviada com sucesso!');
            document.getElementById('denunciaForm').reset();

            // Recarregar lista de denúncias se a função existir
            if (typeof carregarMinhasDenuncias === 'function') {
                carregarMinhasDenuncias();
            }
        } else {
            const errorText = await response.text();
            alert(`Erro ao enviar denúncia: ${errorText}`);
        }
    } catch (error) {
        console.error('Erro ao enviar denúncia:', error);
        alert('Erro de conexão ao enviar denúncia.');
    }
}

// Função para formatação de data
function formatarData(dateString) {
    try {
        const date = new Date(dateString);
        return date.toLocaleDateString('pt-BR');
    } catch (error) {
        return 'Data inválida';
    }
}

// Função para validar email
function validarEmail(email) {
    const regex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    return regex.test(email);
}

// Função para limpar formulário
function limparFormulario(formId) {
    const form = document.getElementById(formId);
    if (form) {
        form.reset();
    }
}

// Função para mostrar loading
function mostrarLoading(elementId, mensagem = 'Carregando...') {
    const element = document.getElementById(elementId);
    if (element) {
        element.innerHTML = `<p class="text-white/70 text-center">${mensagem}</p>`;
    }
}

// Função para mostrar erro
function mostrarErro(elementId, mensagem = 'Erro ao carregar dados.') {
    const element = document.getElementById(elementId);
    if (element) {
        element.innerHTML = `<p class="text-red-400/80 text-center">${mensagem}</p>`;
    }
}

// Função de debounce para evitar múltiplas chamadas
function debounce(func, wait) {
    let timeout;
    return function executedFunction(...args) {
        const later = () => {
            clearTimeout(timeout);
            func(...args);
        };
        clearTimeout(timeout);
        timeout = setTimeout(later, wait);
    };
}

// Função para verificar se o token está próximo de expirar
function verificarExpiracaoToken() {
    const token = getAuthToken();
    if (!token) return false;

    const payload = decodeJWTToken(token);
    if (!payload || !payload.exp) return false;

    const now = Math.floor(Date.now() / 1000);
    const timeLeft = payload.exp - now;

    // Se restam menos de 5 minutos, alertar o usuário
    if (timeLeft < 300) {
        const minutos = Math.floor(timeLeft / 60);
        alert(`Sua sessão expirará em ${minutos} minutos. Salve seu trabalho.`);
        return true;
    }

    return false;
}

// Verificar expiração a cada 5 minutos
setInterval(verificarExpiracaoToken, 300000);

// Exportar funções para uso global
window.ativoOperante = {
    logout,
    verificarAutenticacao,
    makeAuthenticatedRequest,
    getUserIdFromToken,
    getUserLevelFromToken,
    formatarData,
    validarEmail,
    limparFormulario,
    mostrarLoading,
    mostrarErro,
    debounce
};