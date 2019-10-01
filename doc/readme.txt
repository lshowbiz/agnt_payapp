 ########   spring 注解描述      ########
 
@Controller：用于标识是处理器类;
@RequestMapping：请求到处理器功能方法的映射规则;
@RequestParam：请求参数到处理器功能处理方法的方法参数上的绑定;
@ModelAttribute：请求参数到命令对象的绑定;
@SessionAttributes：用于声明session级别存储的属性，放置在处理器类上，通常列出模型属性（如@ModelAttribute）对应的名称，则这些属性会透明的保存到session中;
@InitBinder：自定义数据绑定注册支持，用于将请求参数转换到命令对象属性的对应类型;
@CookieValue：cookie数据到处理器功能处理方法的方法参数上的绑定;
@RequestHeader：请求头（header）数据到处理器功能处理方法的方法参数上的绑定;
@RequestBody：请求的body体的绑定（通过HttpMessageConverter进行类型转换）;
@ResponseBody：处理器功能处理方法的返回值作为响应体（通过HttpMessageConverter进行类型转换）;
@ResponseStatus：定义处理器功能处理方法/异常处理器返回的状态码和原因;
@ExceptionHandler：注解式声明异常处理器;
@PathVariable：请求URI中的模板变量部分到处理器功能处理方法的方法参数上的绑定，从而支持RESTful架构风格的URI;
@ResponseBody: 把返回值转换成对应的数据格式，输出给客户端;
@Resource: 默认按 byName自动注入。@Resource有两个属性是比较重要的，分是name和type，
	Spring将@Resource注解的name属性解析为bean的名字，而type属性则解析为bean的类型。
	所以如果使用name属性，则使用byName的自动注入策略，而使用type属性时则使用byType自动注入策略。
	如果既不指定name也不指定type属性，这时将通过反射机制使用byName自动注入策略
	@Resource(name="dataSource")
　　　@Resource(type=DataSource.class)
@Autowired: 按byType自动注入
@Controller ,@Service ,@Repository类似，都是向spring 上下文中注册bean