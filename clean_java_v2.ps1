$javaFiles = Get-ChildItem -Recurse -Filter "*.java" | Select-Object -ExpandProperty FullName

function Remove-EmojisFromStrings {
    param([string]$text)

    $result = ""
    $i = 0
    $chars = $text.ToCharArray()
    $inString = $false
    $stringChar = $null

    while ($i -lt $chars.Length) {
        $c = $chars[$i]

        if (($c -eq '"' -or $c -eq "'") -and ($i -eq 0 -or $chars[$i-1] -ne '\')) {
            if (-not $inString) {
                $inString = $true
                $stringChar = $c
                $result += $c
            }
            elseif ($c -eq $stringChar) {
                $inString = $false
                $stringChar = $null
                $result += $c
            }
            else {
                $result += $c
            }
        }
        elseif ($inString) {
            $codePoint = [int][char]$c
            if (($codePoint -ge 0x1F300 -and $codePoint -le 0x1F9FF) -or
                ($codePoint -ge 0x2600 -and $codePoint -le 0x26FF) -or
                ($codePoint -ge 0x2700 -and $codePoint -le 0x27BF) -or
                ($codePoint -eq 0x2B50) -or
                ($c -match '\p{So}')) {
            }
            else {
                $result += $c
            }
        }
        else {
            $result += $c
        }

        $i++
    }

    return $result
}

$processedCount = 0

foreach ($file in $javaFiles) {
    $content = Get-Content -Path $file -Raw -Encoding UTF8

    if ($null -eq $content) { continue }

    $content = Remove-EmojisFromStrings -text $content

    Set-Content -Path $file -Value $content -Encoding UTF8 -NoNewline
    $processedCount++
}

Write-Output "Processed $processedCount Java files"
Write-Output "Emojis removed from strings!"
