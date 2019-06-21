package net.youngdev.maven.plugins.reporting;

import java.io.File;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.maven.artifact.DependencyResolutionRequiredException;
import org.apache.maven.doxia.sink.Sink;
import org.apache.maven.plugin.logging.Log;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.plugins.annotations.ResolutionScope;
import org.apache.maven.project.MavenProject;
import org.apache.maven.reporting.AbstractMavenReport;
import org.apache.maven.reporting.MavenReportException;
import org.codehaus.plexus.util.ExceptionUtils;

import guru.nidi.graphviz.attribute.Font;
import guru.nidi.graphviz.engine.Format;
import guru.nidi.graphviz.engine.Graphviz;
import guru.nidi.graphviz.model.MutableGraph;
import guru.nidi.graphviz.parse.Parser;

@Mojo(name = "simple", defaultPhase = LifecyclePhase.SITE, requiresDependencyResolution = ResolutionScope.RUNTIME, requiresProject = true, threadSafe = true)
public class JPA2ERDReport extends AbstractMavenReport {

	public String getOutputName() {
		// This report will generate simple-report.html when invoked in a project with
		// `mvn site`
		return "jpa2erd";
	}

	public String getName(Locale locale) {
		// Name of the report when listed in the project-reports.html page of a project
		return "JPA2ERD Report";
	}

	public String getDescription(Locale locale) {
		// Description of the report when listed in the project-reports.html page of a
		// project
		return "Simple report that uses Graphviz (pure java impl) to convert JPA entities to ERD";
	}

	/**
	 * Practical reference to the Maven project
	 */
	@Parameter(defaultValue = "${project}", readonly = true)
	private MavenProject project;
	
	/**
	 * Practical reference to the Maven project
	 */
	@Parameter(property="entityPackage", 
			//defaultValue = "${entityPackage}", 
			required = true)
	private String  entityPackage;
	
	/**
	   * Where the HTML pages of the report will be created.
	   */
	  @Parameter( defaultValue = "${project.reporting.outputDirectory}", property = "outputDirectory", required = true )
	  private File outputDirectory;
	  /**
	   * 
	   */
	  @Parameter(required = true, defaultValue="${project.build.sourceDirectory}")
	  private File sourceDirectory;
	  
	  /**
	   * The classpath elements of the project.
	   * 
	   * @parameter expression="${project.runtimeClasspathElements}"
	   * @required
	   * @readonly
	   */

	  @Parameter(required = true, defaultValue="${project.runtimeClasspathElements}")
	  private List<String> classpathElements;

//	/**
//	 * The Maven settings
//	 *
//	 * @parameter expression="${settings}
//	 */
//	@Parameter(defaultValue = "${setting}")
//	private Settings settings;
//
//	/**
//	 * The Maven session
//	 *
//	 * @parameter expression="${session}
//	 */
//	@Parameter(defaultValue = "${session}")
//	private MavenSession session;

	@Override
	protected void executeReport(Locale locale) throws MavenReportException {

		// Get the logger
		Log logger = getLog();
		
		List<String> sourceRoots = new ArrayList<>();
		try {
			sourceRoots= project.getCompileClasspathElements();
		} catch (DependencyResolutionRequiredException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
//		for (String srcRoot: sourceRoots) {
//			logger.info(srcRoot);
//		}
		

		// Some info
		logger.info(
				"Generating " + getOutputName() + ".html" + " for " + project.getName() + " " + project.getVersion());

		// Get the Maven Doxia Sink, which will be used to generate the
		// various elements of the document
		Sink mainSink = getSink();
		if (mainSink == null) {
			throw new MavenReportException("Could not get the Doxia sink");
		}

		String dotGraph = "";
		try {
			dotGraph = generateGraphviz(logger, entityPackage, sourceRoots);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			logger.error("Exception e",e1);
		}

		// Page title
		mainSink.head();
		mainSink.title();
		mainSink.text("JPA2ERD Report for " + project.getName() + " " + project.getVersion());
		mainSink.title_();
		mainSink.head_();

		mainSink.body();
		// Generate image
		
		// Heading 1
		mainSink.section1();
		mainSink.sectionTitle1();
		mainSink.text("JPA2ERD Report for " + project.getName() + " " + project.getVersion());
		mainSink.sectionTitle1_();

		// Content
		
		logger.info(dotGraph);
		try {
		MutableGraph g = Parser.read(dotGraph);
//        		Graphviz.fromGraph(g).width(700).render(Format.PNG).toFile(new File("example/ex4-1.png"));
//        		Graphviz g = Graphviz.fromGraph(graph()
//        		        .nodeAttr().with(Font.name("casual"), Shape.RECTANGLE)
//        		        .with(width.link(center)));
		g.nodes().forEach(node -> node.add(Font.name("Courier New")).add(Font.size(14)));

		// g.graphAttrs()
		// .add(Color.WHITE.gradient(Color.rgb("888888")).background().angle(90))
		// .nodeAttrs().add(Color.WHITE.fill())
		// .nodes().forEach(node ->
		// node.add(
		// Color.named(node.name().toString()),
		// Style.lineWidth(4).and(Style.FILLED)));
		File outFile = new File(outputDirectory.getAbsolutePath()+File.separator+"JPA-to-ERD/db_erd.png");
		outFile.getParentFile().mkdirs();
		Graphviz.fromGraph(g)
				// .width(8048).height(6024)
				.fontAdjust(.87)
				// .width(700)
				.render(Format.PNG).toFile(outFile);
			mainSink.paragraph();
			mainSink.figureGraphics("JPA-to-ERD/db_erd.png", null);
			mainSink.rawText(StringEscapeUtils.escapeHtml(dotGraph).replace("\n","<br/>"));
		//	mainSink.text();
			mainSink.paragraph_();


		} catch (Exception e) {
			mainSink.paragraph();
			mainSink.text("This page provides simple information, like its location: ");
			mainSink.text(ExceptionUtils.getFullStackTrace(e));
			mainSink.paragraph_();

			
		}
		
		// Close
		mainSink.section1_();
		mainSink.body_();

	}

	public String generateGraphviz(Log logger,String jpaEntityPackage, List<String> sourceRoots) throws DependencyResolutionRequiredException, MalformedURLException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {

	//	List<String> sourceRoots = project.getCompileSourceRoots();
		
		List<Class<?>> classes =new ArrayList<>() ;
		classes.addAll(ClassFinder.find(logger,jpaEntityPackage, sourceRoots.get(0), getProjectClassLoader()));
		Collections.reverse(classes);
		StringBuilder sb = new StringBuilder();
		Map<String, String> references = new HashMap<String, String>();
		sb.append(getGraphDefinition());
		for (Class<?> c : classes) {
			logger.info("attempting to generate node for "+c.getName());
			sb.append(generateTableNode(logger, c, references));
		}
		for (Map.Entry<String, String> ref : references.entrySet()) {
			sb.append(ref.getKey() + " -> " + ref.getValue() + " [taillabel=\""
					+ StringUtils.split(ref.getKey(), ":")[1] + "\"");
			sb.append(" arrowhead=\"normal\" arrowtail=\"crow\" dir=\"both\" ");

			if (ref.getValue().contains("client:id")) {

				sb.append("  constraint=false");
			}
			sb.append("];\n");
		}
		sb.append("}");

		return sb.toString();
	}

	private String generateTableNode(Log logger, Class<?> c, Map<String, String> references) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
		StringBuilder sb = new StringBuilder();
//		Annotation[] anns = c.getAnnotations();
//		for (Annotation a: anns) {
//		if (a.annotationType().getName().equals("Table")) {
//			for (Field fld: a.getClass().getFields()) {
//				logger.info(a.getClass().getName()+ " . " +fld.getName());
//			}
//		} else {
//			logger.info("found "+a.annotationType().getCanonicalName());
//		}
//		}
		
		String tableName = (String) ReflexiveAnnotationUtils.getAnnotationPropertyForClass(c, "javax.persistence.Table", "name");
		logger.info(tableName);
		if (StringUtils.isNotEmpty(tableName)) {
			sb.append(tableName);
			sb.append(" [label=<\n");
			sb.append("<table border=\"0\" cellspacing=\"0\" cellborder=\"1\">\n");
			sb.append("<tr><td bgcolor=\"#B19CD9\" align=\"left\" colspan=\"2\">" + tableName + "</td></tr>\n");
			Field[] flds = c.getDeclaredFields();

			for (Field fld : flds) {
				if (!Modifier.isStatic(fld.getModifiers()) && !Modifier.isTransient(fld.getModifiers())
					&& !Collection.class.isAssignableFrom(fld.getType()) && hasAnyOfAnnotation(c,fld, "javax.persistence.Column","javax.persistence.JoinColumn")) {
					sb.append(buildNodeRowMarkup(c,tableName,fld,references));
					
				}
			}
			sb.append("</table>>];\n\n");
		}
		return sb.toString();
	}

	private boolean hasAnyOfAnnotation(Class<?> c, Field fld, String... annoClassNames) {
		boolean foundAny=false;
		for (String anno: annoClassNames) {
			Annotation a =  ReflexiveAnnotationUtils.getAnnotationForBeanProperty(c, fld.getName(),
					anno);
			if (a !=null) {
				foundAny = true;
				break;
			}
		}
		return foundAny;
	}

	private Object buildNodeRowMarkup(Class<?> c,String tableName, Field fld,Map<String,String> references) throws  IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException, IllegalAccessException {
		StringBuilder sb = new StringBuilder();
		sb.append("<tr>");
		Annotation col = ReflexiveAnnotationUtils.getAnnotationForBeanProperty(c, fld.getName(), "javax.persistence.Column");
		if (col == null) {
			// sb.append("<tr><td> Column name missing: "+fld.getName()+"</td></tr>\n");
			
			Annotation join = ReflexiveAnnotationUtils.getAnnotationForBeanProperty(c, fld.getName(),
					"javax.persistence.JoinColumn");
			if (join != null) {
				sb.append("<td port=\"" + join.annotationType().getMethod("name").invoke(join) + "\" align=\"left\">" + join.annotationType().getMethod("name").invoke(join)
						+ "</td>");
				String refTable = (String) ReflexiveAnnotationUtils
						.getAnnotationPropertyForClass(fld.getType(), "javax.persistence.Table", "name");
				references.put(tableName + ":" + join.annotationType().getMethod("name").invoke(join), refTable + ":" + join.annotationType().getMethod("referencedColumnName").invoke(join));

			}
		} else {
			sb.append("<td port=\"" + col.annotationType().getMethod("name").invoke(col) + "\" align=\"left\">" + col.annotationType().getMethod("name").invoke(col) + "</td>");

		}
		sb.append("<td align=\"left\">"+StringUtils.trim(buildTypeDef(c,fld))+"</td>");
		sb.append("</tr>\n");
		return sb.toString();
	}

	private String buildTypeDef(Class<?> c, Field fld) {
		StringBuilder sb = new StringBuilder();
		Annotation id = ReflexiveAnnotationUtils.getAnnotationForBeanProperty(c, fld.getName(), "javax.persistence.Id");
		Annotation joinCol = ReflexiveAnnotationUtils.getAnnotationForBeanProperty(c, fld.getName(), "javax.persistence.JoinColumn");

		if (id != null) {
			sb.append("(pk) ");
		} 
		if (joinCol != null) {
			try {
				String refCol = (String)joinCol.annotationType().getMethod("referencedColumnName").invoke(joinCol);
				if ( StringUtils.isNotEmpty(refCol)) {
					sb.append("(fk) ");
					for (Field f: fld.getType().getDeclaredFields()) {
						Annotation refId = ReflexiveAnnotationUtils.getAnnotationForBeanProperty(fld.getType(), f.getName(), "javax.persistence.Id");
						if (refId != null) {
							sb.append(getType(fld.getType(), f));
							break;
						}
					}
					
				}
				} catch (Exception e) {
					
				}
		} else {
			sb.append(getType(c, fld));
		}
		return sb.toString();
	}
	
	public String getType(Class<?> c, Field fld) {
		Annotation col = ReflexiveAnnotationUtils.getAnnotationForBeanProperty(c, fld.getName(), "javax.persistence.Column");

	
		String length = StringUtils.EMPTY;
		String colDefinition = StringUtils.EMPTY;
		try {
		colDefinition = (String)col.annotationType().getMethod("columnDefinition").invoke(col);
		
		} catch (Exception e) {
			
		}
		try {
			length = ""+(int)col.annotationType().getMethod("length").invoke(col);
			
			} catch (Exception e) {
				
			}
		if (StringUtils.isEmpty(colDefinition)) {
			if ( Number.class.isAssignableFrom(fld.getType())) {
	
			colDefinition = "numeric";
			} else if (Date.class.isAssignableFrom(fld.getType())) {
				colDefinition = "date";
				//Annotation temporal = ReflexiveAnnotationUtils.getAnnotationForBeanProperty(c, fld.getName(), "javax.persistence.Temporal");
				
			} else if (CharSequence.class.isAssignableFrom(fld.getType())) {
				colDefinition = "varchar";
				if (StringUtils.isNotEmpty(length)) {
					colDefinition+="("+length+")";
				}

				
			} else {
				colDefinition = fld.getType().getSimpleName().toLowerCase();
			}
			
		}
		return colDefinition;
			
	}

	private String getGraphDefinition() {
		return "digraph {\n"
				// +"splines=false;"
				// +"splines=ortho;\n"
				// +" graph [pad=\"0.5\", nodesep=\"0.5\", ranksep=\"2\"];\n"
				+ " graph [pad=\"0.5\", nodesep=\"2\"];\n" + " node [shape=plain];\n"
		// +" rankdir=LR;\n\n";
		;
	}
	
	private ClassLoader getProjectClassLoader()
		    throws DependencyResolutionRequiredException, MalformedURLException
		{
		    List<String> classPath = new ArrayList<String>();
		    classPath.addAll( classpathElements );
		    getLog().info("Adding output dir to classpath = "+project.getBuild().getOutputDirectory());
		    classPath.add( project.getBuild().getOutputDirectory() );
		    
		    URL[] urls = new URL[classPath.size()];
		    int i = 0;
		    for ( String entry : classPath )
		    {
		        getLog().debug( "use classPath entry " + entry );
		        urls[i] = new File( entry ).toURI().toURL();
		        i++; // Important
		    }
		    return new URLClassLoader( urls );
		}

}