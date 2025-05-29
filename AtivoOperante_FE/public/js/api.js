//arquivo que vai ser utilizado para conectar o frontEnd com o banco de dados

// Configuração base da API
const API_BASE_URL = "http://localhost:8080";

// Função para obter o token do localStorage
function getToken() {
    return localStorage.getItem("token");
}

// Função para obter headers com autenticação
function getAuthHeaders() {
    const token = getToken();
    return {
        "Content-Type": "application/json",
        ...(token && { "Authorization": `Bearer ${token}` })
    };
}

// Função para tratar erros da API
function handleApiError(error, defaultMessage = "Erro ao conectar com o servidor") {
    console.error("Erro na API:", error);
    return defaultMessage;
}

// ==================== AUTENTICAÇÃO ====================

// Função de Login
function login(email, senha) {
    return fetch(`${API_BASE_URL}/api/public/login`, {
        method: "POST",
        headers: {
            "Content-Type": "application/json",
        },
        body: JSON.stringify({ email, senha }),
    })
        .then(async (res) => {
            const texto = await res.text();
            if (res.ok) {
                try {
                    const json = JSON.parse(texto);
                    localStorage.setItem("token", json.token);
                    localStorage.setItem("userLevel", json.nivel);
                    return { success: true, data: json };
                } catch {
                    return { success: false, message: "Resposta inválida do servidor" };
                }
            } else {
                return { success: false, message: "Credenciais inválidas!" };
            }
        })
        .catch((err) => {
            return { success: false, message: handleApiError(err) };
        });
}

// Função de Registro
function register(email, senha, cpf) {
    const cpfNumerico = cpf.replace(/\D/g, ''); // Remove formatação do CPF

    return fetch(`${API_BASE_URL}/api/public/register`, {
        method: "POST",
        headers: {
            "Content-Type": "application/json",
        },
        body: JSON.stringify({ email, senha, cpf: parseInt(cpfNumerico) }),
    })
        .then(async (res) => {
            const texto = await res.text();
            if (res.ok) {
                return { success: true, message: "Usuário cadastrado com sucesso!" };
            } else {
                return { success: false, message: texto || "Erro ao cadastrar usuário" };
            }
        })
        .catch((err) => {
            return { success: false, message: handleApiError(err) };
        });
}

// Função de Logout
function logout() {
    localStorage.removeItem("token");
    localStorage.removeItem("userLevel");
    window.location.href = "login.html";
}

// ==================== DENÚNCIAS ====================

// Listar todas as denúncias (Admin)
function getAllDenuncias() {
    return fetch(`${API_BASE_URL}/apis/admin/denuncia`, {
        method: "GET",
        headers: getAuthHeaders(),
    })
        .then(res => res.json())
        .catch(err => handleApiError(err));
}

// Buscar denúncia por ID
function getDenunciaById(id) {
    const userLevel = localStorage.getItem("userLevel");
    const endpoint = userLevel === "1" ?
        `${API_BASE_URL}/apis/admin/denuncia/${id}` :
        `${API_BASE_URL}/apis/cidadao/denuncia/${id}`;

    return fetch(endpoint, {
        method: "GET",
        headers: getAuthHeaders(),
    })
        .then(res => res.json())
        .catch(err => handleApiError(err));
}

// Adicionar nova denúncia
function addDenuncia(denuncia) {
    return fetch(`${API_BASE_URL}/apis/cidadao/denuncia`, {
        method: "POST",
        headers: getAuthHeaders(),
        body: JSON.stringify(denuncia),
    })
        .then(async (res) => {
            if (res.ok) {
                return { success: true, data: await res.json() };
            } else {
                const error = await res.text();
                return { success: false, message: error };
            }
        })
        .catch(err => {
            return { success: false, message: handleApiError(err) };
        });
}

// Atualizar denúncia
function updateDenuncia(denuncia) {
    const userLevel = localStorage.getItem("userLevel");
    const endpoint = userLevel === "1" ?
        `${API_BASE_URL}/apis/admin/denuncia` :
        `${API_BASE_URL}/apis/cidadao/denuncia`;

    return fetch(endpoint, {
        method: "PUT",
        headers: getAuthHeaders(),
        body: JSON.stringify(denuncia),
    })
        .then(async (res) => {
            if (res.ok) {
                return { success: true, data: await res.json() };
            } else {
                const error = await res.text();
                return { success: false, message: error };
            }
        })
        .catch(err => {
            return { success: false, message: handleApiError(err) };
        });
}

// Deletar denúncia
function deleteDenuncia(id) {
    const userLevel = localStorage.getItem("userLevel");
    const endpoint = userLevel === "1" ?
        `${API_BASE_URL}/apis/admin/denuncia/${id}` :
        `${API_BASE_URL}/apis/cidadao/denuncia/${id}`;

    return fetch(endpoint, {
        method: "DELETE",
        headers: getAuthHeaders(),
    })
        .then(res => {
            if (res.ok) {
                return { success: true };
            } else {
                return { success: false, message: "Erro ao deletar denúncia" };
            }
        })
        .catch(err => {
            return { success: false, message: handleApiError(err) };
        });
}

// Buscar denúncias por usuário
function getDenunciasByUsuario(userId) {
    const userLevel = localStorage.getItem("userLevel");
    const endpoint = userLevel === "1" ?
        `${API_BASE_URL}/apis/admin/denuncia/usuario/${userId}` :
        `${API_BASE_URL}/apis/cidadao/denuncia/usuario/${userId}`;

    return fetch(endpoint, {
        method: "GET",
        headers: getAuthHeaders(),
    })
        .then(res => res.json())
        .catch(err => handleApiError(err));
}

// Adicionar feedback (apenas Admin)
function addFeedback(denunciaId, texto) {
    return fetch(`${API_BASE_URL}/apis/admin/denuncia/admin/add-feedback/${denunciaId}/${texto}`, {
        method: "GET",
        headers: getAuthHeaders(),
    })
        .then(res => {
            if (res.ok) {
                return { success: true };
            } else {
                return { success: false, message: "Erro ao adicionar feedback" };
            }
        })
        .catch(err => {
            return { success: false, message: handleApiError(err) };
        });
}

// ==================== USUÁRIOS ====================

// Listar todos os usuários (Admin)
function getAllUsuarios() {
    return fetch(`${API_BASE_URL}/apis/admin/usuario`, {
        method: "GET",
        headers: getAuthHeaders(),
    })
        .then(res => res.json())
        .catch(err => handleApiError(err));
}

// Buscar usuário por ID
function getUsuarioById(id) {
    const userLevel = localStorage.getItem("userLevel");
    const endpoint = userLevel === "1" ?
        `${API_BASE_URL}/apis/admin/usuario/${id}` :
        `${API_BASE_URL}/apis/cidadao/usuario/${id}`;

    return fetch(endpoint, {
        method: "GET",
        headers: getAuthHeaders(),
    })
        .then(res => res.json())
        .catch(err => handleApiError(err));
}

// Adicionar usuário (Admin)
function addUsuario(usuario) {
    return fetch(`${API_BASE_URL}/apis/admin/usuario`, {
        method: "POST",
        headers: getAuthHeaders(),
        body: JSON.stringify(usuario),
    })
        .then(async (res) => {
            if (res.ok) {
                return { success: true, data: await res.json() };
            } else {
                const error = await res.text();
                return { success: false, message: error };
            }
        })
        .catch(err => {
            return { success: false, message: handleApiError(err) };
        });
}

// Atualizar usuário
function updateUsuario(usuario) {
    const userLevel = localStorage.getItem("userLevel");
    const endpoint = userLevel === "1" ?
        `${API_BASE_URL}/apis/admin/usuario` :
        `${API_BASE_URL}/apis/cidadao/usuario`;

    return fetch(endpoint, {
        method: "PUT",
        headers: getAuthHeaders(),
        body: JSON.stringify(usuario),
    })
        .then(async (res) => {
            if (res.ok) {
                return { success: true, data: await res.json() };
            } else {
                const error = await res.text();
                return { success: false, message: error };
            }
        })
        .catch(err => {
            return { success: false, message: handleApiError(err) };
        });
}

// Deletar usuário
function deleteUsuario(id) {
    const userLevel = localStorage.getItem("userLevel");
    const endpoint = userLevel === "1" ?
        `${API_BASE_URL}/apis/admin/usuario/${id}` :
        `${API_BASE_URL}/apis/cidadao/usuario/${id}`;

    return fetch(endpoint, {
        method: "DELETE",
        headers: getAuthHeaders(),
    })
        .then(res => {
            if (res.ok) {
                return { success: true };
            } else {
                return { success: false, message: "Erro ao deletar usuário" };
            }
        })
        .catch(err => {
            return { success: false, message: handleApiError(err) };
        });
}

// ==================== ÓRGÃOS (Admin apenas) ====================

// Listar todos os órgãos
function getAllOrgaos() {
    return fetch(`${API_BASE_URL}/apis/admin/orgaos`, {
        method: "GET",
        headers: getAuthHeaders(),
    })
        .then(res => res.json())
        .catch(err => handleApiError(err));
}

// Buscar órgão por ID
function getOrgaoById(id) {
    return fetch(`${API_BASE_URL}/apis/admin/orgaos/${id}`, {
        method: "GET",
        headers: getAuthHeaders(),
    })
        .then(res => res.json())
        .catch(err => handleApiError(err));
}

// Adicionar órgão
function addOrgao(orgao) {
    return fetch(`${API_BASE_URL}/apis/admin/orgaos`, {
        method: "POST",
        headers: getAuthHeaders(),
        body: JSON.stringify(orgao),
    })
        .then(async (res) => {
            if (res.ok) {
                return { success: true, data: await res.json() };
            } else {
                const error = await res.text();
                return { success: false, message: error };
            }
        })
        .catch(err => {
            return { success: false, message: handleApiError(err) };
        });
}

// Atualizar órgão
function updateOrgao(orgao) {
    return fetch(`${API_BASE_URL}/apis/admin/orgaos`, {
        method: "PUT",
        headers: getAuthHeaders(),
        body: JSON.stringify(orgao),
    })
        .then(async (res) => {
            if (res.ok) {
                return { success: true, data: await res.json() };
            } else {
                const error = await res.text();
                return { success: false, message: error };
            }
        })
        .catch(err => {
            return { success: false, message: handleApiError(err) };
        });
}

// Deletar órgão
function deleteOrgao(id) {
    return fetch(`${API_BASE_URL}/apis/admin/orgaos/${id}`, {
        method: "DELETE",
        headers: getAuthHeaders(),
    })
        .then(res => {
            if (res.ok) {
                return { success: true };
            } else {
                return { success: false, message: "Erro ao deletar órgão" };
            }
        })
        .catch(err => {
            return { success: false, message: handleApiError(err) };
        });
}

// ==================== TIPOS (Admin apenas) ====================

// Listar todos os tipos
function getAllTipos() {
    return fetch(`${API_BASE_URL}/apis/admin/tipo`, {
        method: "GET",
        headers: getAuthHeaders(),
    })
        .then(res => res.json())
        .catch(err => handleApiError(err));
}

// Buscar tipo por ID
function getTipoById(id) {
    return fetch(`${API_BASE_URL}/apis/admin/tipo/${id}`, {
        method: "GET",
        headers: getAuthHeaders(),
    })
        .then(res => res.json())
        .catch(err => handleApiError(err));
}

// Adicionar tipo
function addTipo(tipo) {
    return fetch(`${API_BASE_URL}/apis/admin/tipo`, {
        method: "POST",
        headers: getAuthHeaders(),
        body: JSON.stringify(tipo),
    })
        .then(async (res) => {
            if (res.ok) {
                return { success: true, data: await res.json() };
            } else {
                const error = await res.text();
                return { success: false, message: error };
            }
        })
        .catch(err => {
            return { success: false, message: handleApiError(err) };
        });
}

// Atualizar tipo
function updateTipo(tipo) {
    return fetch(`${API_BASE_URL}/apis/admin/tipo`, {
        method: "PUT",
        headers: getAuthHeaders(),
        body: JSON.stringify(tipo),
    })
        .then(async (res) => {
            if (res.ok) {
                return { success: true, data: await res.json() };
            } else {
                const error = await res.text();
                return { success: false, message: error };
            }
        })
        .catch(err => {
            return { success: false, message: handleApiError(err) };
        });
}

// Deletar tipo
function deleteTipo(id) {
    return fetch(`${API_BASE_URL}/apis/admin/tipo/${id}`, {
        method: "DELETE",
        headers: getAuthHeaders(),
    })
        .then(res => {
            if (res.ok) {
                return { success: true };
            } else {
                return { success: false, message: "Erro ao deletar tipo" };
            }
        })
        .catch(err => {
            return { success: false, message: handleApiError(err) };
        });
}

// ==================== EVENT LISTENERS ====================

// Event listener para login
document.addEventListener("DOMContentLoaded", () => {
    const loginForm = document.querySelector("form");

    // Verificar se estamos na página de login
    if (loginForm && document.getElementById("emailLogin")) {
        loginForm.addEventListener("submit", async (event) => {
            event.preventDefault();

            const email = document.getElementById("emailLogin").value;
            const senha = document.getElementById("senhaLogin").value;

            const result = await login(email, senha);

            if (result.success) {
                // Redirecionar baseado no nível do usuário
                if (result.data.nivel === 1) {
                    window.location.href = "main_adm.html";
                } else {
                    window.location.href = "main_cidadao.html";
                }
            } else {
                alert(result.message);
            }
        });
    }

    // Event listener para cadastro
    if (loginForm && document.getElementById("email") && document.getElementById("cpf")) {
        loginForm.addEventListener("submit", async (event) => {
            event.preventDefault();

            const email = document.getElementById("email").value;
            const senha = document.getElementById("senha").value;
            const cpf = document.getElementById("cpf").value;

            const result = await register(email, senha, cpf);

            if (result.success) {
                alert(result.message);
                window.location.href = "login.html";
            } else {
                alert(result.message);
            }
        });
    }

    // Verificar se o usuário está logado
    const token = getToken();
    if (token && (window.location.pathname.includes("login.html") || window.location.pathname.includes("cadastro.html"))) {
        const userLevel = localStorage.getItem("userLevel");
        if (userLevel === "1") {
            window.location.href = "main_adm.html";
        } else {
            window.location.href = "main_cidadao.html";
        }
    }
});

// Verificar autenticação nas páginas protegidas
function checkAuth() {
    const token = getToken();
    const protectedPages = ["main_adm.html", "main_cidadao.html"];
    const currentPage = window.location.pathname.split("/").pop();

    if (protectedPages.includes(currentPage) && !token) {
        window.location.href = "login.html";
        return false;
    }
    return true;
}

// Chamar verificação de autenticação ao carregar páginas protegidas
if (document.readyState === "loading") {
    document.addEventListener("DOMContentLoaded", checkAuth);
} else {
    checkAuth();
}