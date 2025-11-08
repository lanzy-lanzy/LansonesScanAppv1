# Gemini AI Prompts - LansonesScanApp

This document contains the exact prompts used in the LansonesScanApp for analyzing lansones fruits and leaves.

## 🍇 Fruit Identification Prompt

### Purpose
Identify lansones fruit variety, assess ripeness, and detect defects.

### Full Prompt

```
You are a tropical fruit expert specializing in lansones (lanzones) fruit identification.

Analyze the uploaded image of a lansones fruit and respond ONLY in valid JSON format with the following structure:
{
  "variety": "string",
  "confidence": number,
  "ripeness": "string",
  "defects": ["string"],
  "notes": "string"
}

Guidelines:
- variety: Identify the variety (e.g., "Lonkong", "Duco", "Paete", "Unknown")
- confidence: Provide a confidence score from 0 to 100
- ripeness: Classify as "unripe", "ripe", "overripe", or "damaged"
- defects: List any visible defects such as bruising, insect holes, discoloration
- notes: Provide additional remarks or suggestions

If you cannot identify the variety with certainty, set variety to "Unknown" and explain why in the notes field.

Respond ONLY with valid JSON, no additional text.
```

### Expected Response Format

```json
{
  "variety": "Lonkong",
  "confidence": 85,
  "ripeness": "ripe",
  "defects": ["minor bruising on skin"],
  "notes": "High-quality fruit with characteristic translucent flesh"
}
```

### Variety Types
- **Lonkong**: Most popular, sweet, minimal seeds
- **Duco**: Larger, slightly sour
- **Paete**: Medium-sized, balanced sweetness
- **Unknown**: Cannot determine variety

### Ripeness Levels
- **unripe**: Green/yellowish, firm
- **ripe**: Golden yellow, slightly soft
- **overripe**: Dark yellow/brown, very soft
- **damaged**: Visible damage or decay

---

## 🌿 Leaf Disease Detection Prompt

### Purpose
Diagnose lansones leaf diseases and provide actionable treatment recommendations.

### Full Prompt

```
You are a plant pathologist specializing in tropical crops, particularly lansones (lanzones) trees.

Analyze the uploaded image of a lansones leaf and respond ONLY in valid JSON format with the following structure:
{
  "diagnosis": [
    {
      "name": "string",
      "confidence": number
    }
  ],
  "primary_diagnosis": "string",
  "severity": "string",
  "recommended_treatment": ["string"],
  "recommended_products": [
    {
      "type": "string",
      "example": "string"
    }
  ],
  "cultural_recommendations": ["string"],
  "additional_info_needed": "string"
}

Guidelines:
- diagnosis: List all possible diseases/conditions with confidence scores (0-100)
- primary_diagnosis: The most likely diagnosis
- severity: Classify as "low", "moderate", or "high"
- recommended_treatment: Provide actionable, short treatment steps
- recommended_products: Suggest product types and examples (e.g., fungicides, pesticides)
- cultural_recommendations: Provide farming best practices to prevent recurrence
- additional_info_needed: If uncertain, specify what additional information would help

Common lansones leaf diseases include:
- Leaf spot
- Powdery mildew
- Anthracnose
- Bacterial blight
- Nutrient deficiency

If you cannot make a confident diagnosis, indicate "uncertain" as primary_diagnosis and list what additional information is needed.

Respond ONLY with valid JSON, no additional text.
```

### Expected Response Format

```json
{
  "diagnosis": [
    {
      "name": "Leaf spot disease",
      "confidence": 75
    },
    {
      "name": "Fungal infection",
      "confidence": 60
    }
  ],
  "primary_diagnosis": "Leaf spot disease",
  "severity": "moderate",
  "recommended_treatment": [
    "Remove affected leaves",
    "Apply fungicide spray",
    "Improve air circulation"
  ],
  "recommended_products": [
    {
      "type": "Fungicide",
      "example": "Copper-based fungicide"
    },
    {
      "type": "Organic treatment",
      "example": "Neem oil spray"
    }
  ],
  "cultural_recommendations": [
    "Avoid overhead watering",
    "Maintain proper spacing between trees",
    "Remove fallen leaves regularly"
  ],
  "additional_info_needed": ""
}
```

### Common Diseases

#### Leaf Spot
- **Symptoms**: Circular brown/black spots
- **Cause**: Fungal or bacterial
- **Treatment**: Fungicide, remove affected leaves

#### Powdery Mildew
- **Symptoms**: White powdery coating
- **Cause**: Fungal
- **Treatment**: Sulfur-based fungicide, improve air circulation

#### Anthracnose
- **Symptoms**: Dark lesions, leaf curling
- **Cause**: Fungal (Colletotrichum)
- **Treatment**: Copper fungicide, prune affected areas

#### Bacterial Blight
- **Symptoms**: Water-soaked lesions, yellowing
- **Cause**: Bacterial
- **Treatment**: Copper bactericide, remove infected parts

#### Nutrient Deficiency
- **Symptoms**: Yellowing, stunted growth
- **Cause**: Lack of nitrogen, iron, or other nutrients
- **Treatment**: Balanced fertilizer, soil testing

### Severity Levels
- **low**: Minor symptoms, <10% leaf coverage
- **moderate**: Noticeable symptoms, 10-30% coverage
- **high**: Severe symptoms, >30% coverage, tree health at risk

---

## Prompt Engineering Tips

### For Better Fruit Analysis
1. **Clear Images**: Ensure good lighting and focus
2. **Multiple Angles**: Include close-ups of skin texture
3. **Color Reference**: Natural lighting shows true color
4. **Size Reference**: Include a common object for scale

### For Better Leaf Analysis
1. **Full Leaf View**: Show entire leaf when possible
2. **Close-ups**: Capture details of spots or discoloration
3. **Both Sides**: Top and bottom of leaf if possible
4. **Context**: Include surrounding leaves if showing spread

### Improving Accuracy
- Use high-resolution images (at least 1080p)
- Avoid blurry or dark images
- Include multiple symptoms if present
- Provide context (e.g., recent weather, tree age)

---

## Customizing Prompts

### Adding New Varieties

To add support for new lansones varieties, update the fruit prompt:

```
- variety: Identify the variety (e.g., "Lonkong", "Duco", "Paete", "NewVariety", "Unknown")
```

Add description in guidelines:
```
Common varieties:
- Lonkong: Sweet, minimal seeds, translucent flesh
- Duco: Larger, slightly sour
- Paete: Medium-sized, balanced
- NewVariety: [Add characteristics here]
```

### Adding New Diseases

To add support for new diseases, update the leaf prompt:

```
Common lansones leaf diseases include:
- Leaf spot
- Powdery mildew
- Anthracnose
- Bacterial blight
- Nutrient deficiency
- New Disease Name
```

Add description:
```
- New Disease Name: [Symptoms, cause, treatment]
```

### Adjusting Confidence Thresholds

In `GeminiClient.kt`, you can filter results by confidence:

```kotlin
val fruitResult = geminiClient.parseFruitResult(jsonResponse)
if (fruitResult.confidence < 50) {
    // Low confidence, ask for better image
}
```

---

## Testing Prompts

### Test Cases for Fruit Analysis

**Test 1: Perfect Lonkong**
- Image: Ripe, golden yellow Lonkong fruit
- Expected: variety="Lonkong", confidence>80, ripeness="ripe"

**Test 2: Damaged Fruit**
- Image: Fruit with visible bruising
- Expected: defects list includes "bruising", ripeness="damaged"

**Test 3: Unripe Fruit**
- Image: Green, firm fruit
- Expected: ripeness="unripe", notes about waiting to harvest

### Test Cases for Leaf Analysis

**Test 1: Healthy Leaf**
- Image: Green, unblemished leaf
- Expected: primary_diagnosis="healthy" or "no disease detected"

**Test 2: Leaf Spot**
- Image: Leaf with circular brown spots
- Expected: primary_diagnosis includes "leaf spot", severity based on coverage

**Test 3: Nutrient Deficiency**
- Image: Yellowing leaf with green veins
- Expected: primary_diagnosis="nutrient deficiency", specific nutrient mentioned

---

## Prompt Versioning

### Version 1.0 (Current)
- Basic fruit variety identification
- Common disease detection
- Treatment recommendations

### Future Versions

**Version 1.1 (Planned)**
- Multi-fruit detection in single image
- Pest identification on leaves
- Soil condition recommendations

**Version 2.0 (Planned)**
- Growth stage assessment
- Harvest timing recommendations
- Yield predictions
- Climate-specific advice

---

## Prompt Performance Metrics

### Accuracy Targets
- Fruit variety identification: >85% accuracy
- Ripeness assessment: >90% accuracy
- Disease detection: >75% accuracy
- Treatment recommendations: 100% actionable

### Response Time
- Target: <5 seconds per analysis
- Typical: 2-3 seconds
- Max acceptable: 10 seconds

### JSON Validity
- Target: 100% valid JSON responses
- Current: ~95% (cleaned with markdown removal)
- Fallback: Error handling for invalid responses

---

## Contributing to Prompts

To improve prompts:

1. **Test with Real Images**: Use actual lansones photos
2. **Collect Feedback**: From farmers and agricultural experts
3. **Iterate**: Refine based on accuracy metrics
4. **Document**: Update this file with changes
5. **Version**: Track prompt versions in git

### Prompt Improvement Checklist
- [ ] Tested with 10+ diverse images
- [ ] Validated by agricultural expert
- [ ] JSON schema matches data models
- [ ] Error cases handled gracefully
- [ ] Documentation updated

---

## Resources

- [Gemini Prompt Best Practices](https://ai.google.dev/docs/prompt_best_practices)
- [JSON Schema Validation](https://json-schema.org/)
- [Lansones Cultivation Guide](https://www.example.com/lansones-guide)
- [Plant Disease Identification](https://www.example.com/plant-diseases)

---

**Last Updated**: November 2024  
**Prompt Version**: 1.0  
**Gemini Model**: gemini-1.5-flash
