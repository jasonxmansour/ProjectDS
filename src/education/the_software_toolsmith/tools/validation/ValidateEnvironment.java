/* @formatter:off
 *
 * © David M Rosenberg, The Software Toolsmith (education)
 *
 * This file is part of the Testing Framework for Java.
 * Repository: https://github.com/The-Software-Toolsmith/testing-framework-for-java
 *
 * Licensed under the Creative Commons Attribution-NonCommercial 4.0 International License.
 * You may obtain a copy of the license at:
 *     https://creativecommons.org/licenses/by-nc/4.0/
 *
 * You may use, share, and adapt this file for non-commercial purposes,
 * provided you give appropriate credit.
 *
 * @formatter:on
 */

package education.the_software_toolsmith.tools.validation ;

//import java.io.File ;
import java.nio.file.Path ;

/**
 * validate the project environment and name
 * 
 * @author David M Rosenberg
 * 
 * @version 1.0 2025-09-07 Initial implementation - merged
 *     {@code CheckEnvironment} and {@code ValidateProjectName}
 * @version 1.1 2025-09-30
 *     <ul>
 *     <li>reworked messages wrt Java version to be consistent with project name
 *     check
 *     <li>temporarily removed path length check
 *     </ul>
 */
public final class ValidateEnvironment
    {
    
    /** class can't be instantiated */
    private ValidateEnvironment()
        {}
    

    /**
     * display information about the runtime environment including
     * <ul>
     * <li>the version of the JDK/JRE we're running on
     * <li>the full path to this project
     * <li>the full path to this class
     * <li>whether the project name is correct/acceptable
     * </ul>
     * 
     * @param args
     *     -unused-
     */
    public static void main( String[] args )
        {

        final int minimumVersion = 24 ;
        
        System.out.printf( "validate Java version%n%n" ) ;
        
        System.out.printf( "\tJava %d is the minimum required version%n%n",
                           minimumVersion ) ;

        System.out.printf( "\tyou are running Java %s%n%n\t%s%n",
                           Runtime.version().toString(),
                           ( Runtime.version().feature() >= minimumVersion
                               ? "you're all set"
                               : String.format( "you need to install Java %d and configure your IDE to use it",
                                                minimumVersion ) ) ) ;
        
//        final String classPath = System.getProperty( "java.class.path" ) ;
//        final String fullClassName = ValidateEnvironment.class.getName().replace( '.', File.separatorChar ) ;
//        final String fullClassPath = classPath + File.separator + fullClassName ;
//        final int fullClassPathLength = fullClassPath.length() ;
//        
//        System.out.printf( """
//                           
//                           your code will be running from:
//                           \t%s
//                           \tthe length of this path is %,d characters
//                           
//                           \tthe maximum path length on Windows is 260 characters
//                           
//                           \tthe full path to this class is %s
//                           \twhich is %,d characters
//                           
//                           \t%s
//                           """,
//                           classPath,
//                           classPath.length(),
//                           fullClassPath,
//                           fullClassPathLength,
//                           String.format( (260 - fullClassPathLength ) <= 30
//                                              ? "you may have trouble with 'path too long' errors%n\t\tmove your Eclipse workspace to a folder closer to the root folder"
//                                              : "you should be ok and probably won't encounter 'path too long' errors" ) ) ;
        
        
        System.out.printf( "%n%nvalidate project name%n" ) ;
        
        validateProjectName() ;
        
        }   // end main()
    


    /**
     * validate the current project name - determine if it appears to be the starter project:
     * <ul>
     * <li>contains 'username'
     * <li>contains 'group 00'
     * </ul>
     */
    public static void validateProjectName()
        {

        final Path currentWorkingDirectoryPath = Path.of( System.getProperty( "user.dir" ) ) ;

        String projectName = currentWorkingDirectoryPath.getName( currentWorkingDirectoryPath
                                                                      .getNameCount() - 1 )
                                                         .toString() ;
        if ( "bin".equalsIgnoreCase( projectName ) ||
             "src".equalsIgnoreCase( projectName ) )
            {
            projectName = currentWorkingDirectoryPath.getName( currentWorkingDirectoryPath
                                                                   .getNameCount() - 2 )
                                                     .toString() ;
            }
        final String projectNameForComparison = projectName.toLowerCase() ;

        System.out.printf( """
                           
                               your project is named: "%s"
                           
                           """,
                           projectName ) ;

        final int usernameIsAt = projectNameForComparison.indexOf( "username" ) ;
        final int groupIsAt = projectNameForComparison.indexOf( "group 00" ) ;

        if ( usernameIsAt >= 0 )
            {

            System.out.printf( """
                                   you must replace "%s"
                                       with your username (for example, RosenbergD)

                               """, projectName.substring( usernameIsAt ) ) ;
            }
        else if ( groupIsAt >= 0 )
            {

            System.out.printf( """
                                   you must replace "%s"
                                       with "Group ##" where ## is your group number (2 digits with leading 0-fill)

                               """,
                               projectName.substring( groupIsAt ) ) ;

            }
        else
            {

            System.out.printf( """
                                   looks good
                               
                               """ ) ;

            }

        }   // end validateProjectName()

    }   // end class CheckEnvironment