--
-- PostgreSQL database dump
--

-- Dumped from database version 15.8
-- Dumped by pg_dump version 16.4

-- Started on 2025-05-01 17:05:40

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- TOC entry 214 (class 1259 OID 16673)
-- Name: denuncia; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.denuncia (
    den_id bigint NOT NULL,
    den_titulo character varying(255),
    den_texto character varying(255),
    den_urgencia integer,
    org_id integer,
    den_data date,
    tip_id bigint,
    usu_id integer
);

CREATE TABLE public.denuncia_imagem (
                                        id bigint NOT NULL GENERATED ALWAYS AS IDENTITY,
                                        denuncia_id bigint UNIQUE,
                                        nome character varying(255),
                                        tipo character varying(255),
                                        tamanho bigint,
                                        dados bytea,
                                        data_upload timestamp without time zone DEFAULT now(),
                                        CONSTRAINT denuncia_imagem_pkey PRIMARY KEY (id),
                                        CONSTRAINT fk_denuncia_id FOREIGN KEY (denuncia_id)
                                            REFERENCES public.denuncia (den_id) MATCH SIMPLE
                                            ON UPDATE NO ACTION ON DELETE NO ACTION
);

ALTER TABLE public.denuncia_imagem OWNER TO postgres;


ALTER TABLE public.denuncia OWNER TO postgres;

--
-- TOC entry 215 (class 1259 OID 16678)
-- Name: denuncia_den_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.denuncia_den_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.denuncia_den_id_seq OWNER TO postgres;

--
-- TOC entry 3371 (class 0 OID 0)
-- Dependencies: 215
-- Name: denuncia_den_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.denuncia_den_id_seq OWNED BY public.denuncia.den_id;


--
-- TOC entry 216 (class 1259 OID 16679)
-- Name: feedback; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.feedback (
    fee_id integer NOT NULL,
    fee_texto text,
    den_id integer
);


ALTER TABLE public.feedback OWNER TO postgres;

--
-- TOC entry 217 (class 1259 OID 16684)
-- Name: feedback_fee_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.feedback_fee_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.feedback_fee_id_seq OWNER TO postgres;

--
-- TOC entry 3372 (class 0 OID 0)
-- Dependencies: 217
-- Name: feedback_fee_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.feedback_fee_id_seq OWNED BY public.feedback.fee_id;


--
-- TOC entry 218 (class 1259 OID 16685)
-- Name: orgaos; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.orgaos (
    org_id bigint NOT NULL,
    org_nome character varying(255)
);


ALTER TABLE public.orgaos OWNER TO postgres;

--
-- TOC entry 219 (class 1259 OID 16688)
-- Name: orgaos_org_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.orgaos_org_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.orgaos_org_id_seq OWNER TO postgres;

--
-- TOC entry 3373 (class 0 OID 0)
-- Dependencies: 219
-- Name: orgaos_org_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.orgaos_org_id_seq OWNED BY public.orgaos.org_id;


--
-- TOC entry 220 (class 1259 OID 16689)
-- Name: tipo; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.tipo (
    tip_id bigint NOT NULL,
    tip_nome character varying(255)
);


ALTER TABLE public.tipo OWNER TO postgres;

--
-- TOC entry 221 (class 1259 OID 16692)
-- Name: tipo_tip_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.tipo_tip_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.tipo_tip_id_seq OWNER TO postgres;

--
-- TOC entry 3374 (class 0 OID 0)
-- Dependencies: 221
-- Name: tipo_tip_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.tipo_tip_id_seq OWNED BY public.tipo.tip_id;


--
-- TOC entry 222 (class 1259 OID 16693)
-- Name: usuario; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.usuario (
    usu_id bigint NOT NULL,
    usu_cpf character varying(255) NOT NULL,
    usu_email character varying(255) NOT NULL,
    usu_senha character varying(255) NOT NULL,
    usu_nivel integer NOT NULL
);


ALTER TABLE public.usuario OWNER TO postgres;

--
-- TOC entry 223 (class 1259 OID 16696)
-- Name: usuario_usu_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.usuario_usu_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.usuario_usu_id_seq OWNER TO postgres;

--
-- TOC entry 3375 (class 0 OID 0)
-- Dependencies: 223
-- Name: usuario_usu_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.usuario_usu_id_seq OWNED BY public.usuario.usu_id;


--
-- TOC entry 3193 (class 2604 OID 16732)
-- Name: denuncia den_id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.denuncia ALTER COLUMN den_id SET DEFAULT nextval('public.denuncia_den_id_seq'::regclass);


--
-- TOC entry 3194 (class 2604 OID 16698)
-- Name: feedback fee_id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.feedback ALTER COLUMN fee_id SET DEFAULT nextval('public.feedback_fee_id_seq'::regclass);


--
-- TOC entry 3195 (class 2604 OID 16775)
-- Name: orgaos org_id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.orgaos ALTER COLUMN org_id SET DEFAULT nextval('public.orgaos_org_id_seq'::regclass);


--
-- TOC entry 3196 (class 2604 OID 16787)
-- Name: tipo tip_id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.tipo ALTER COLUMN tip_id SET DEFAULT nextval('public.tipo_tip_id_seq'::regclass);


--
-- TOC entry 3197 (class 2604 OID 16799)
-- Name: usuario usu_id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.usuario ALTER COLUMN usu_id SET DEFAULT nextval('public.usuario_usu_id_seq'::regclass);


--
-- TOC entry 3356 (class 0 OID 16673)
-- Dependencies: 214
-- Data for Name: denuncia; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.denuncia (den_id, den_titulo, den_texto, den_urgencia, org_id, den_data, tip_id, usu_id) FROM stdin;
1	semaforo quebrado	semaforo da av da saudede perto do campua I da Unoeste não está funcionando	4	4	2023-05-12	1	2
\.


--
-- TOC entry 3358 (class 0 OID 16679)
-- Dependencies: 216
-- Data for Name: feedback; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.feedback (fee_id, fee_texto, den_id) FROM stdin;
1	sua requisição será atendida em 24 horas	1
\.


--
-- TOC entry 3360 (class 0 OID 16685)
-- Dependencies: 218
-- Data for Name: orgaos; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.orgaos (org_id, org_nome) FROM stdin;
1	SEDUC
2	Policia militar
3	Policia Civil
4	SETRAN
\.


--
-- TOC entry 3362 (class 0 OID 16689)
-- Dependencies: 220
-- Data for Name: tipo; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.tipo (tip_id, tip_nome) FROM stdin;
1	trânsito
2	educação
3	ambiental
4	segurança
\.


--
-- TOC entry 3364 (class 0 OID 16693)
-- Dependencies: 222
-- Data for Name: usuario; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.usuario (usu_id, usu_cpf, usu_email, usu_senha, usu_nivel) FROM stdin;
1	12111158963	admin@pm.br	123321	1
2	5488889915	ze@cidadao.com.br	123	2
\.


--
-- TOC entry 3376 (class 0 OID 0)
-- Dependencies: 215
-- Name: denuncia_den_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.denuncia_den_id_seq', 2, true);


--
-- TOC entry 3377 (class 0 OID 0)
-- Dependencies: 217
-- Name: feedback_fee_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.feedback_fee_id_seq', 1, true);


--
-- TOC entry 3378 (class 0 OID 0)
-- Dependencies: 219
-- Name: orgaos_org_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.orgaos_org_id_seq', 5, true);


--
-- TOC entry 3379 (class 0 OID 0)
-- Dependencies: 221
-- Name: tipo_tip_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.tipo_tip_id_seq', 5, true);


--
-- TOC entry 3380 (class 0 OID 0)
-- Dependencies: 223
-- Name: usuario_usu_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.usuario_usu_id_seq', 3, true);


--
-- TOC entry 3199 (class 2606 OID 16734)
-- Name: denuncia denuncia_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.denuncia
    ADD CONSTRAINT denuncia_pkey PRIMARY KEY (den_id);


--
-- TOC entry 3201 (class 2606 OID 16705)
-- Name: feedback feedback_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.feedback
    ADD CONSTRAINT feedback_pkey PRIMARY KEY (fee_id);


--
-- TOC entry 3205 (class 2606 OID 16777)
-- Name: orgaos orgaos_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.orgaos
    ADD CONSTRAINT orgaos_pkey PRIMARY KEY (org_id);


--
-- TOC entry 3207 (class 2606 OID 16789)
-- Name: tipo tipo_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.tipo
    ADD CONSTRAINT tipo_pkey PRIMARY KEY (tip_id);


--
-- TOC entry 3203 (class 2606 OID 16832)
-- Name: feedback unique_den_id; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.feedback
    ADD CONSTRAINT unique_den_id UNIQUE (den_id) INCLUDE (den_id);


--
-- TOC entry 3209 (class 2606 OID 16801)
-- Name: usuario usuario_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.usuario
    ADD CONSTRAINT usuario_pkey PRIMARY KEY (usu_id);


--
-- TOC entry 3210 (class 2606 OID 16778)
-- Name: denuncia denuncia_org_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.denuncia
    ADD CONSTRAINT denuncia_org_id_fkey FOREIGN KEY (org_id) REFERENCES public.orgaos(org_id) NOT VALID;


--
-- TOC entry 3211 (class 2606 OID 16790)
-- Name: denuncia denuncia_tip_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.denuncia
    ADD CONSTRAINT denuncia_tip_id_fkey FOREIGN KEY (tip_id) REFERENCES public.tipo(tip_id) NOT VALID;


--
-- TOC entry 3212 (class 2606 OID 16802)
-- Name: denuncia denuncia_usu_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.denuncia
    ADD CONSTRAINT denuncia_usu_id_fkey FOREIGN KEY (usu_id) REFERENCES public.usuario(usu_id) NOT VALID;


--
-- TOC entry 3213 (class 2606 OID 16735)
-- Name: feedback feedback_den_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.feedback
    ADD CONSTRAINT feedback_den_id_fkey FOREIGN KEY (den_id) REFERENCES public.denuncia(den_id);


-- Completed on 2025-05-01 17:05:40

--
-- PostgreSQL database dump complete
--

