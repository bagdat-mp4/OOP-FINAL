$javaFiles = Get-ChildItem -Recurse -Filter "*.java" | Select-Object -ExpandProperty FullName

$processedCount = 0

foreach ($file in $javaFiles) {
    $content = Get-Content -Path $file -Raw -Encoding UTF8

    $content = $content -replace '/\*\*[\s\S]*?\*/', ''

    $content = $content -replace '/\*[\s\S]*?\*/', ''

    $lines = $content -split "`n"
    $cleanedLines = @()

    foreach ($line in $lines) {
        $inString = $false
        $chars = $line.ToCharArray()
        $newLine = ""
        $i = 0

        while ($i -lt $chars.Length) {
            $char = $chars[$i]

            if ($char -eq '"' -and ($i -eq 0 -or $chars[$i-1] -ne '\')) {
                $inString = -not $inString
                $newLine += $char
                $i++
                continue
            }

            if (-not $inString -and $i -lt $chars.Length - 1) {
                if ($chars[$i] -eq '/' -and $chars[$i+1] -eq '/') {
                    break
                }
            }

            $newLine += $char
            $i++
        }

        $cleanedLines += $newLine
    }

    $content = $cleanedLines -join "`n"

    $content = $content -replace '\p{So}', ''

    $content = $content -replace '(?m)^\s*$(\r?\n){2,}', "`n`n"

    Set-Content -Path $file -Value $content -Encoding UTF8 -NoNewline
    $processedCount++
}

Write-Output "Processed $processedCount Java files"
Write-Output "Comments and emojis removed!"
