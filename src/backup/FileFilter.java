package backup;


	

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
 
import static java.nio.file.FileVisitResult.*;
 
public class FileFilter implements FileVisitor<Path>
{
    private PathMatcher matcher;
    private List<File> files= new ArrayList<>();
    private HashMap<File,String> filesPaths= new HashMap<>();
    public FileFilter(String regularExpression)
    {
        matcher= FileSystems.getDefault().getPathMatcher("glob:" + regularExpression);
    }
 
    public void findFile(Path file)
    {
        Path fileName=file.getFileName();
        if(fileName!=null && matcher.matches(fileName))
        {
            files.add(fileName.toFile());
            filesPaths.put(fileName.toFile(),file.toString());
        }
    }
    public List<File> getFiles()
    {
        return files;
    }
    public HashMap<File,String> getFilesPaths(){return filesPaths;}
    @Override
    public FileVisitResult visitFile(Path file, BasicFileAttributes bfa)
    {
        findFile(file);
        return CONTINUE;
    }
 
    @Override
    public FileVisitResult preVisitDirectory(Path dir,BasicFileAttributes bfa)
    {
        findFile(dir);
        return CONTINUE;
    }
 
    @Override
    public FileVisitResult postVisitDirectory(Path dir, IOException exc)
    {
        findFile(dir);
        return CONTINUE;
    }
 
    @Override
    public FileVisitResult visitFileFailed(Path file,IOException exc)
    {
        System.out.println(exc);
        return CONTINUE;
    }
}


