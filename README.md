# JPA-to-ERD-reporting-plugin
This is a general plugin for creating a Vendor agnostic (Logical) ERD from JPA entities by reading the annotations.  
The resulting diagram should be logically correct even if not applicable to a given DB Vendor.  

Here is sample report screen shot:
![report screenshot](./report_screenshot.png)

## how to enable it 
include this fragment as your reporting section:
```xml

	<reporting>
		<plugins>
			<plugin>
				<groupId>net.youngdev.maven.plugins</groupId>
				<artifactId>JPA-to-ERD-reporting-plugin</artifactId>
				<version>0.0.9.12</version>
				<configuration>
					<entityPackage>my.entity.package.name</entityPackage>
				</configuration>
			</plugin>

		</plugins>
	</reporting>
```

Possible configuration options are:
- entityPackage - this is the canonical package name which holds the JPA entities
- graphEngine - Possible values are DOT, NEATO, TWOPI, CIRCO, FDP - default is FDP
- graphConfig - This is the beginning portion of the digraph right after the opening curly bracket The default is
```
graph [pad="1.0", nodesep="2", splines=ortho];
node [shape=plain];
repulsiveforce=45;
K=6.5;
start=regular;
```
- arrowHeadStyle - possible values can be found here: https://www.graphviz.org/doc/info/attrs.html#k:arrowType.  My default is "none"
- labelTargetField - boolean whether or not to label the head of the edge (target of the foreign key) with the field name 
- headerColor - Color (in hex) used to shade the entity table name background.  The new default is ![#98BFDA](https://placehold.it/15/98BFDA/000000?text=+) `#98BFDA` based on the style used by MySQLWorkbench.  In case you liked the old default, (like ERMaster), it used to be ![#B19CD9](https://placehold.it/15/B19CD9/000000?text=+) `#B19CD9`
- embedImage - (experimental) boolean value specifying whether the graph images should be base 64 encoded into the report html or linked to the external image.  The default is false

 
## General features
- Supports Relationship detection with directed edges between nodes (crow's foot)
- based on graphviz so the dot layout script is easily pulled from the report
- all non-transient, non-static, JPA annotated fields including (Id, Column, JoinColumn)
- datatype detection
- Primary and Foriegn key detection

## Coming soon
- ~~new license (I'm leaning toward BSD but I need to check all the dependencies first)~~
- Clean up the actual report layout 
- refactor the code.  The code is fugly and I don't wan't future employers to look at it
- Code coverage
- ~~Build a sample reference project~~
- ~~document config~~
- ~~Submit my handy plugin to maven central~~
- Javadocs
- gradle plugin
- Make font configurable
- Unique constraint (From @Table annotation)
- Nullable detection
- numeric scale + precision detection
- Verify mappedsuperclass
- Check nullpointer on ClassFinder:64


## troubleshooting
I've noticed a few peculiarities that require your project to be written with certain conventions in order for the graph to work;
- Every field must meet the following requirements:
	- must have either a @Column annotation or @JoinColumn annoation.
	- field cannot be transient
	- field cannot be annotated as @Transient
	- field cannot be static
	- does not work on getter/setter methods
- the jar containing javax.persistence.* annotation classes must be in compile scope
- make sure the site plugin is included in the build section with this plugin turned on
- If you get a graph where all the nodes and edges are visible but the text is missing, it means your JVM could not find
  the true type font (right now it uses Courier New), you need to install msttcorefonts package and restart the JVM.
  I have only encountered this on linux.  Ubuntu example: apt install msttcorefonts
  
  
