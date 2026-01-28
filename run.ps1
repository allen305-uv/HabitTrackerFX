Clear-Host
Write-Host "--- HABIT TRACKER LAUNCHING ---" -ForegroundColor Cyan
javac src/*.java
if ($?) {
    java -cp src Main
} else {
    Write-Host "Build Failed." -ForegroundColor Red
}