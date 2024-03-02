/*
@ExtendWith(MockitoExtension.class)
class ProdutoControllerTest {
    @Mock
    private ProdutoService produtoService;
    private Produto produto;
    private List<Produto> produtoLista;
    @InjectMocks
    private ProdutoController productController;
    private MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        produto = new Produto(1l, "TV 55", "TV 55 POLEGADAS LED, LG", 500l);
        mockMvc = MockMvcBuilders.standaloneSetup(productController).build();
    }

    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void adicionarProdutoHttpTest() throws Exception{
        //Arrange - Preparar
        when(produtoService.adicionarProduto(Mockito.any())).thenReturn(produto);

        //Act - Açao
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/produtos").
                contentType(MediaType.APPLICATION_JSON).
                content(asJsonString(produto))).andExpect(status().isCreated());

        //Assertion - Validacao
        verify(produtoService, times(1)).adicionarProduto(Mockito.any());
    }

    @Test
    public void listarProdutosHttpTest() throws Exception {
        when(produtoService.listarProdutos()).thenReturn(produtoLista);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/produtos").
                        contentType(MediaType.APPLICATION_JSON).
                        content(asJsonString(produto))).
                andDo(MockMvcResultHandlers.print());

        verify(produtoService,times(1)).listarProdutos();
    }

}*/
